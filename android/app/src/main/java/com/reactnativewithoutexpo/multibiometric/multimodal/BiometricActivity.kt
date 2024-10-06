package com.reactnativewithoutex

import android.view.*
import com.neurotec.licensing.gui.ActivationActivity
import com.neurotec.samples.app.DirectoryViewer
import com.neurotec.samples.app.InfoActivity
import com.neurotec.samples.licensing.LicensingState
import com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.neurotec.biometrics.*
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.lang.NCore
import com.neurotec.samples.app.BaseActivity
import com.neurotec.samples.licensing.LicensingManager
import com.neurotec.util.concurrent.CompletionHandler
import com.reactnativewithoutexpo.R
import com.reactnativewithoutexpo.multibiometric.Model
import com.reactnativewithoutexpo.multibiometric.view.EnrollmentDialogFragment
import com.reactnativewithoutexpo.multibiometric.view.SubjectListFragment
import java.beans.PropertyChangeListener
import java.util.*

abstract class BiometricActivity : BaseActivity(), EnrollmentDialogFragment.EnrollmentDialogListener,
    SubjectListFragment.SubjectSelectionListener,
    LicensingManager.LicensingStateCallback {
    // ===========================================================
    // Private fields
    // ===========================================================
    private val subjectListHandler: CompletionHandler<Array<NSubject?>?, NBiometricOperation?> =
        object : CompletionHandler<Array<NSubject?>?, NBiometricOperation?> {
            override fun completed(result: Array<NSubject?>?, attachment: NBiometricOperation?) {
                Model.getInstance().setSubjects(result)
            }

            override fun failed(exc: Throwable, attachment: NBiometricOperation?) {
                Log.e(TAG, exc.toString(), exc)
            }
        }

    private val completionHandler: CompletionHandler<NBiometricTask, NBiometricOperation> =
        object : CompletionHandler<NBiometricTask, NBiometricOperation> {
            override fun completed(task: NBiometricTask, operation: NBiometricOperation) {
                var message: String? = null
                val status: NBiometricStatus = task.getStatus()
                Log.i(TAG, String.format("Operation: %s, Status: %s", operation, status))

                onOperationCompleted(operation, task)
                if (status == NBiometricStatus.CANCELED) return

                if (task.getError() != null) {
                    showError(task.getError())
                } else {
                    subject = task.getSubjects().get(0)
                    when (operation) {
                        NBiometricOperation.CAPTURE, NBiometricOperation.CREATE_TEMPLATE -> {
                            if (status == NBiometricStatus.OK) {
                                message = getString(R.string.msg_extraction_succeeded)
                            } else if (task.getSubjects().size > 0 && task.getSubjects().get(0)
                                    .getFaces().size > 0 && task.getStatus() == NBiometricStatus.TIMEOUT
                            ) {
                                message = getString(
                                    R.string.msg_extraction_failed,
                                    getString(R.string.msg_liveness_check_failed)
                                )
                            } else {
                                message = getString(R.string.msg_extraction_failed, status.toString())
                            }
                        }

                        NBiometricOperation.ENROLL, NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK -> {
                            if (status == NBiometricStatus.OK) {
                                message = getString(R.string.msg_enrollment_succeeded)
                            } else {
                                message = getString(R.string.msg_enrollment_failed, status.toString())
                            }
                            client.list<NBiometricOperation>(NBiometricOperation.LIST, subjectListHandler)
                        }

                        NBiometricOperation.VERIFY -> {
                            if (status == NBiometricStatus.OK) {
                                message = getString(R.string.msg_verification_succeeded)
                            } else {
                                message = getString(R.string.msg_verification_failed, status.toString())
                            }
                        }

                        NBiometricOperation.IDENTIFY -> {
                            if (status == NBiometricStatus.OK) {
                                val sb = StringBuilder()
                                val subject: NSubject = task.getSubjects().get(0)
                                for (result in subject.getMatchingResults()) {
                                    sb.append(getString(R.string.msg_identification_results, result.getId()))
                                        .append('\n')
                                }
                                message = sb.toString()
                            } else {
                                message = getString(R.string.msg_no_matches)
                            }
                        }

                        else -> {
                            throw AssertionError("Invalid NBiometricOperation")
                        }
                    }
                    showInfo(message)
                }
            }

            override fun failed(th: Throwable, operation: NBiometricOperation) {
                onOperationCompleted(operation, null)
                showError(th)
            }
        }

    private var captureControls: LinearLayout? = null
    private var stopControls: LinearLayout? = null
    private var successControls: LinearLayout? = null

    // ===========================================================
    // Protected fields
    // ===========================================================
    protected var mAppClosing: Boolean = false
    protected var mAppIsGoingToBackground: Boolean = false

    protected var client: NBiometricClient? = null
    protected var subject: NSubject? = null
    protected val biometricPropertyChanged: PropertyChangeListener = PropertyChangeListener { evt ->
        if ("Status" == evt.propertyName) {
            onStatusChanged((evt.source as NBiometric).getStatus())
        }
    }

    // ===========================================================
    // Protected abstract methods
    // ===========================================================
    protected abstract val preferences: Class<*>?

    protected abstract fun updatePreferences(client: NBiometricClient?)
    protected abstract val isCheckForDuplicates: Boolean
    protected abstract val additionalComponents: List<String>
    protected abstract val mandatoryComponents: List<String>
    protected abstract val modalityAssetDirectory: String

    // ===========================================================
    // Protected methods
    // ===========================================================
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NCore.setContext(this)
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setContentView(R.layout.multimodal_main_biometric)
            captureControls = findViewById(R.id.multimodal_capture_controls) as LinearLayout?
            successControls = findViewById(R.id.multimodal_success_controls) as LinearLayout?
            stopControls = findViewById(R.id.multimodal_stop_controls) as LinearLayout?
            val mLoadButton = findViewById(R.id.multimodal_button_load) as Button
            mLoadButton.setOnClickListener { onLoad() }
            val mCaptureButton = findViewById(R.id.multimodal_button_capture) as Button
            mCaptureButton.setOnClickListener { onStartCapturing() }
            val mStopButton = findViewById(R.id.multimodal_button_stop) as Button
            mStopButton.setOnClickListener {
                try {
                    onStopCapturing()
                } catch (e: Exception) {
                    showError(e)
                }
            }
            val mRetryButton = findViewById(R.id.multimodal_button_retry) as Button
            mRetryButton.setOnClickListener { onBack() }
            val mAddButton = findViewById(R.id.multimodal_button_add) as Button
            mAddButton.setOnClickListener { onEnroll() }
            val mDiscardButton = findViewById(R.id.multimodal_button_discard) as Button
            mDiscardButton.setOnClickListener {
                setResult(RESULT_OK)
                finish()
            }
            val mUnboundButton = findViewById(R.id.multimodal_button_unbound) as Button
            mUnboundButton.visibility = View.INVISIBLE
            InitializationTask().execute(savedInstanceState == null)
        } catch (e: Exception) {
            showError(e)
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_GET_FILE) {
            if (resultCode == RESULT_OK) {
                try {
                    onFileSelected(data.data)
                } catch (th: Throwable) {
                    showError(th)
                }
            }
        }
    }

    protected open fun onStartCapturing() {}

    protected fun onStopCapturing() {
        cancel()
    }

    protected open fun onOperationStarted(operation: NBiometricOperation) {
        if (operation == NBiometricOperation.CAPTURE) {
            runOnUiThread(object : Runnable {
                override fun run() {
                    if (isStopSupported) {
                        captureControls?.setVisibility(View.GONE)
                        stopControls?.setVisibility(View.VISIBLE)
                        successControls?.setVisibility(View.GONE)
                    }
                }
            })
        } else {
            if (isActive) {
                showProgress(R.string.msg_processing)
            }
        }
    }

    protected open fun onOperationCompleted(operation: NBiometricOperation, task: NBiometricTask?) {
        hideProgress()
        runOnUiThread(Runnable {
            if (task != null && (task.getStatus() == NBiometricStatus.OK || task.getOperations()
                    .contains(NBiometricOperation.IDENTIFY)
                        || task.getOperations().contains(NBiometricOperation.VERIFY)
                        || task.getOperations().contains(NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK)
                        || task.getOperations().contains(NBiometricOperation.ENROLL))
            ) {
                captureControls.setVisibility(View.GONE)
                stopControls.setVisibility(View.GONE)
                successControls.setVisibility(View.VISIBLE)
            } else {
                stopControls.setVisibility(View.GONE)
                successControls.setVisibility(View.GONE)
                captureControls.setVisibility(View.VISIBLE)
            }
        })
    }

    protected open fun onLicensesObtained() {}

    @Throws(Exception::class)
    protected open fun onFileSelected(uri: Uri?) {
    }

    protected val isActive: Boolean
        get() = client?.getCurrentBiometric() ?: != null || client.getCurrentSubject() != null

    protected open val isStopSupported: Boolean
        get() = true

    protected fun stop() {
        client.force()
    }

    protected override fun onResume() {
        super.onResume()
        mAppIsGoingToBackground = false
    }

    protected fun cancel() {
        if (client != null) {
            client.cancel()
        }
    }

    protected fun onLoad() {
        cancel()
        hideProgress()
        val intent: Intent = Intent(this, DirectoryViewer::class.java)
        intent.putExtra(DirectoryViewer.ASSET_DIRECTORY_LOCATION, modalityAssetDirectory)
        startActivityForResult(intent, REQUEST_CODE_GET_FILE)
    }

    protected fun onBack() {
        runOnUiThread(Runnable {
            captureControls?.setVisibility(View.VISIBLE)
            stopControls?.setVisibility(View.GONE)
            successControls?.setVisibility(View.GONE)
        })
    }

    protected fun onEnroll() {
        EnrollmentDialogFragment().show(getFragmentManager(), "enrollment")
    }

    protected fun onIdentify() {
        if (subject == null) throw NullPointerException("subject")
        val task: NBiometricTask =
            client.createTask(EnumSet.of<NBiometricOperation>(NBiometricOperation.IDENTIFY), subject)
        client.performTask<NBiometricOperation>(task, NBiometricOperation.IDENTIFY, completionHandler)
        onOperationStarted(NBiometricOperation.IDENTIFY)
    }

    protected fun onVerify() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_REQUEST_CODE, VERIFICATION_REQUEST_CODE)
        SubjectListFragment.newInstance(Model.getInstance().getSubjects(), true, bundle)
            .show(getFragmentManager(), "verification")
    }

    protected open fun onStatusChanged(status: NBiometricStatus?) {}

    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onBackPressed() {
        super.onBackPressed()
        mAppClosing = true
    }

    protected override fun onStop() {
        mAppIsGoingToBackground = true
        cancel()
        if (mAppClosing) {
        }
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_preferences -> {
                startActivity(Intent(this, preferences))
            }

            R.id.action_database -> {
                val bundle = Bundle()
                bundle.putInt(EXTRA_REQUEST_CODE, DATABASE_REQUEST_CODE)
                SubjectListFragment.newInstance(Model.getInstance().getSubjects(), false, bundle)
                    .show(getFragmentManager(), "database")
            }

            R.id.action_activation -> {
                val activation: Intent = Intent(this, ActivationActivity::class.java)
                val params = Bundle()
                params.putStringArrayList(
                    ActivationActivity.LICENSES,
                    ArrayList<String>(MultiModalActivity.Companion.getAllComponentsInternal())
                )
                activation.putExtras(params)
                startActivity(activation)
            }

            R.id.action_about -> {
                startActivity(Intent(this, InfoActivity::class.java))
            }
        }
        return true
    }

    override fun onEnrollmentIDProvided(id: String?) {
        subject?.setId(id)
        updatePreferences(client)
        val operation: NBiometricOperation =
            if (isCheckForDuplicates) NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK else NBiometricOperation.ENROLL
        val task: NBiometricTask = client.createTask(EnumSet.of<NBiometricOperation>(operation), subject)
        client.performTask<NBiometricOperation>(task, NBiometricOperation.ENROLL, completionHandler)
        onOperationStarted(NBiometricOperation.ENROLL)
    }

    override fun onSubjectSelected(otherSubject: NSubject, bundle: Bundle) {
        if (bundle.getInt(EXTRA_REQUEST_CODE) == VERIFICATION_REQUEST_CODE) {
            subject.setId(otherSubject.getId())
            updatePreferences(client)
            val task: NBiometricTask =
                client.createTask(EnumSet.of<NBiometricOperation>(NBiometricOperation.VERIFY), subject)
            client.performTask<NBiometricOperation>(task, NBiometricOperation.VERIFY, completionHandler)
            onOperationStarted(NBiometricOperation.VERIFY)
        }
    }

    override fun onLicensingStateChanged(state: LicensingState) {
        when (state) {
            OBTAINING -> showProgress(R.string.msg_obtaining_licenses)
            OBTAINED -> {
                hideProgress()
                showToast(R.string.msg_licenses_obtained)
            }

            NOT_OBTAINED -> {
                hideProgress()
                showToast(R.string.msg_licenses_not_obtained)
            }
        }
    }

    fun capture(subject: NSubject, additionalOperations: EnumSet<NBiometricOperation>?) {
        if (subject == null) throw NullPointerException("subject")
        this.subject = subject
        updatePreferences(client)
        val operations: EnumSet<NBiometricOperation> =
            EnumSet.of<NBiometricOperation>(NBiometricOperation.CREATE_TEMPLATE)
        if (additionalOperations != null) {
            operations.addAll(additionalOperations)
        }
        val task: NBiometricTask = client.createTask(operations, subject)
        client.performTask<NBiometricOperation>(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler)
        onOperationStarted(NBiometricOperation.CAPTURE)
    }

    fun extract(biometric: NBiometric) {
        if (biometric == null) throw NullPointerException("biometric")
        subject.clear()
        updatePreferences(client)
        val task: NBiometricTask =
            client.createTask(EnumSet.of<NBiometricOperation>(NBiometricOperation.CREATE_TEMPLATE), subject)
        task.setBiometric(biometric)
        client.performTask<NBiometricOperation>(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler)
        onOperationStarted(NBiometricOperation.CREATE_TEMPLATE)
    }

    fun extract(subject: NSubject) {
        if (subject == null) throw NullPointerException("subject")
        this.subject = subject
        updatePreferences(client)
        val task: NBiometricTask =
            client.createTask(EnumSet.of<NBiometricOperation>(NBiometricOperation.CREATE_TEMPLATE), subject)
        client.performTask<NBiometricOperation>(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler)
        onOperationStarted(NBiometricOperation.CREATE_TEMPLATE)
    }

    internal inner class InitializationTask : AsyncTask<Any?, Boolean?, Boolean?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(R.string.msg_initializing)
        }

        override fun doInBackground(vararg params: Any): Boolean {
            require(params.size >= 1) { "Missing parameter if to obtain license" }
            showProgress(R.string.msg_initializing_client)

            try {
                client = Model.getInstance().getClient()
                subject = Model.getInstance().getSubject()
                mAppClosing = false
                client.list<NBiometricOperation>(NBiometricOperation.LIST, subjectListHandler)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                return false
            }
            return true
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            hideProgress()
            onLicensesObtained()
        }
    }

    companion object {
        // ===========================================================
        // Private static fields
        // ===========================================================
        private const val REQUEST_CODE_GET_FILE = 1

        private const val EXTRA_REQUEST_CODE = "request_code"
        private const val VERIFICATION_REQUEST_CODE = 1
        private const val DATABASE_REQUEST_CODE = 2

        protected const val RECORD_REQUEST_FACE: String = "face"
        protected const val RECORD_REQUEST_FINGER: String = "finger"

        private val TAG: String = BiometricActivity::class.java.simpleName
    }
}
