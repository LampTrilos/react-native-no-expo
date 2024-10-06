package com.reactnativewithoutexpo.multibiometric.multimodal

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.neurotec.biometrics.*
import com.neurotec.lang.NCore
import com.neurotec.samples.app.BaseActivity
import com.neurotec.util.concurrent.CompletionHandler
import com.reactnativewithoutexpo.multibiometric.Model
import com.reactnativewithoutexpo.multibiometric.preferences.ConnectionPreferences
import com.reactnativewithoutexpo.multibiometric.preferences.MultimodalPreferences
import java.util.*

class MultiModalActivity : BaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    // ===========================================================
    // Private fields
    // ===========================================================
    private val MODALITY_CODE_FACE = 1
    private val MODALITY_CODE_FINGER = 2
    private val MODALITY_CODE_IRIS = 3
    private val MODALITY_CODE_VOICE = 4

    private var mFaces: MutableList<NLRecord>? = null
    private var mFingers: MutableList<NFRecord>? = null
    private var mIris: MutableList<NERecord>? = null
    private var mVoice: MutableList<NSRecord>? = null

    private var mSubjectId: EditText? = null

    private var mFaceCounter: TextView? = null
    private var mFingerCounter: TextView? = null
    private var mIrisCounter: TextView? = null
    private var mVoiceCounter: TextView? = null

    private fun updateRecordCount(
        faces: List<NLRecord>?,
        fingers: List<NFRecord>?,
        iris: List<NERecord>?,
        voice: List<NSRecord>?
    ) {
        if (faces != null) {
            mFaceCounter!!.text = faces.size.toString()
        }
        if (fingers != null) {
            mFingerCounter!!.text = fingers.size.toString()
        }
        if (iris != null) {
            mIrisCounter!!.text = iris.size.toString()
        }
        if (voice != null) {
            mVoiceCounter!!.text = voice.size.toString()
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

    private val notGrantedPermissions: Array<String>
        get() {
            val neededPermissions: MutableList<String> = ArrayList()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    neededPermissions.add(permission)
                }
            }
            return neededPermissions.toTypedArray<String>()
        }

    private fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS)
    }

    private fun verify() {
        val builderSingle = AlertDialog.Builder(this@MultiModalActivity)
        builderSingle.setTitle(
            ("Database elements (" + Model.getInstance().getClient().listIds().length).toString() + ")"
        )

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this@MultiModalActivity, R.layout.select_dialog_singlechoice)
        if (Model.getInstance().getClient().listIds().length > 0) {
            arrayAdapter.addAll(Model.getInstance().getClient().listIds())
            builderSingle.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, item: Int) {
                    val strName: String = arrayAdapter.getItem(item).toString()
                    val subject: NSubject? = createSubjectFromRecords(mFaces!!, mFingers!!, mIris!!, mVoice!!)
                    subject.setId(strName)
                    if (subject != null) {
                        val task: NBiometricTask = Model.getInstance().getClient()
                            .createTask(EnumSet.of<E>(NBiometricOperation.VERIFY), subject)
                        Model.getInstance().getClient().performTask(task, NBiometricOperation.VERIFY, completionHandler)
                    } else {
                        showError("Empty subject")
                    }
                }
            })
        } else {
            arrayAdapter.add("Database is empty")
            builderSingle.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                }
            })
        }

        builderSingle.setNegativeButton("Close", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })

        builderSingle.show()
    }

    private fun showDatabase() {
        val builderSingle = AlertDialog.Builder(this@MultiModalActivity)
        builderSingle.setTitle(
            ("Database elements (" + Model.getInstance().getClient().listIds().length).toString() + ")"
        )

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this@MultiModalActivity, R.layout.select_dialog_singlechoice)
        if (Model.getInstance().getClient().listIds().length > 0) {
            arrayAdapter.addAll(Model.getInstance().getClient().listIds())

            builderSingle.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, item: Int) {
                    val strName: String = arrayAdapter.getItem(item)
                    val element = strName
                    val builderInner = AlertDialog.Builder(this@MultiModalActivity)

                    val subject: NSubject = NSubject()
                    subject.setId(strName)
                    val status: NBiometricStatus = Model.getInstance().getClient().get(subject)

                    val sb = StringBuilder()
                    sb.append("Subject ID: ")
                    sb.append(strName)
                    if (status == NBiometricStatus.OK) {
                        sb.append("\n")
                        if (subject.getTemplate() != null) {
                            if (subject.getTemplate().getFaces() != null) {
                                for (record in subject.getTemplate().getFaces().getRecords()) {
                                    sb.append("\tFace record, quality: ")
                                    sb.append(record.getQuality().toInt())
                                    sb.append("\n")
                                }
                            }
                            if (subject.getTemplate().getFingers() != null) {
                                for (record in subject.getTemplate().getFingers().getRecords()) {
                                    sb.append("\tFinger, quality: ")
                                    sb.append(record.getQuality().toInt())
                                    sb.append(", position: ")
                                    sb.append(toLowerCase(record.getPosition().name))
                                    sb.append("\n")
                                }
                            }
                            if (subject.getTemplate().getIrises() != null) {
                                for (record in subject.getTemplate().getIrises().getRecords()) {
                                    sb.append("\tIris record, quality: ")
                                    sb.append(record.getQuality().toInt())
                                    sb.append("\n")
                                }
                            }
                            if (subject.getTemplate().getVoices() != null) {
                                for (record in subject.getTemplate().getVoices().getRecords()) {
                                    sb.append("\tVoice record, quality: ")
                                    sb.append(record.getQuality().toInt())
                                    sb.append("\n")
                                }
                            }
                        }
                    }

                    builderInner.setMessage(sb.toString())
                    builderInner.setTitle("Do you wish to delete item?")
                    builderInner.setPositiveButton("Delete", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Model.getInstance().getClient().delete(element)
                        }
                    })
                    builderInner.setNegativeButton("Close", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            dialogInterface.dismiss()
                        }
                    })
                    builderInner.show()
                }
            })
        } else {
            arrayAdapter.add("Database is empty")
            builderSingle.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                }
            })
        }

        builderSingle.setNegativeButton("Close", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })
        builderSingle.show()
    }

    private fun createSubjectFromRecords(
        faces: List<NLRecord>,
        fingers: List<NFRecord>,
        irises: List<NERecord>,
        voices: List<NSRecord>
    ): NSubject? {
        if (!faces.isEmpty() || !fingers.isEmpty() || !irises.isEmpty() || !voices.isEmpty()) {
            val subject: NSubject = NSubject()
            val template: NTemplate = NTemplate()
            val faceTemplate: NLTemplate = NLTemplate()
            for (record in faces) {
                faceTemplate.getRecords().add(record)
            }
            template.setFaces(faceTemplate)

            val fingerTemplate: NFTemplate = NFTemplate()
            for (record in fingers) {
                fingerTemplate.getRecords().add(record)
            }
            template.setFingers(fingerTemplate)

            val irisTemplate: NETemplate = NETemplate()
            for (record in irises) {
                irisTemplate.getRecords().add(record)
            }
            template.setIrises(irisTemplate)

            val voiceTemplate: NSTemplate = NSTemplate()
            for (record in voices) {
                voiceTemplate.getRecords().add(record)
            }
            template.setVoices(voiceTemplate)

            subject.setTemplate(template)
            return subject
        } else {
            return null
        }
    }

    private val completionHandler: CompletionHandler<NBiometricTask, NBiometricOperation> =
        object : CompletionHandler<NBiometricTask, NBiometricOperation?> {
            override fun completed(task: NBiometricTask, operation: NBiometricOperation?) {
                try {
                    var message: String? = null
                    val status: NBiometricStatus = task.getStatus()
                    Log.i(TAG, String.format("Operation: %s, Status: %s", operation, status))

                    if (status == NBiometricStatus.CANCELED) return

                    if (task.getError() != null) {
                        showError(task.getError())
                    } else {
                        when (operation) {
                            NBiometricOperation.ENROLL, NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK -> {
                                if (status == NBiometricStatus.OK) {
                                    message = getString(R.string.msg_enrollment_succeeded)
                                } else {
                                    message = getString(R.string.msg_enrollment_failed, status.toString())
                                }
                            }

                            NBiometricOperation.IDENTIFY -> {
                                if (status == NBiometricStatus.OK) {
                                    val sb = StringBuilder()
                                    val subject: NSubject = task.getSubjects().get(0)
                                    for (result in subject.getMatchingResults()) {
                                        sb.append(
                                            "MATCHED WITH: " + getString(
                                                R.string.msg_identification_results,
                                                result.getId()
                                            )
                                        ).append('\n')
                                        sb.append("\tMatching score: " + result.getScore() + "\n")
                                        if (result.getMatchingDetails() != null && !result.getMatchingDetails()
                                                .getFaces().isEmpty()
                                        ) {
                                            var index = 0
                                            sb.append(
                                                "\tFaces fused score: " + result.getMatchingDetails()
                                                    .getFacesScore() + "\n"
                                            )
                                            for (score in result.getMatchingDetails().getFaces()) {
                                                sb.append("\t\tNL " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n")
                                                index++
                                            }
                                        }
                                        if (result.getMatchingDetails() != null && !result.getMatchingDetails()
                                                .getFingers().isEmpty()
                                        ) {
                                            var index = 0
                                            sb.append(
                                                "\tFingers fused score: " + result.getMatchingDetails()
                                                    .getFingersScore() + "\n"
                                            )
                                            for (score in result.getMatchingDetails().getFingers()) {
                                                sb.append("\t\tNF " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n")
                                                index++
                                            }
                                        }
                                        if (result.getMatchingDetails() != null && !result.getMatchingDetails()
                                                .getIrises().isEmpty()
                                        ) {
                                            var index = 0
                                            sb.append(
                                                "\tIrises fused score: " + result.getMatchingDetails()
                                                    .getIrisesScore() + "\n"
                                            )
                                            for (score in result.getMatchingDetails().getIrises()) {
                                                sb.append("\t\tNI " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n")
                                                index++
                                            }
                                        }
                                        if (result.getMatchingDetails() != null && !result.getMatchingDetails()
                                                .getVoices().isEmpty()
                                        ) {
                                            var index = 0
                                            sb.append(
                                                "\tVoices fused score: " + result.getMatchingDetails()
                                                    .getVoicesScore() + "\n"
                                            )
                                            for (score in result.getMatchingDetails().getVoices()) {
                                                sb.append("\t\tNS " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n")
                                                index++
                                            }
                                        }
                                        sb.append("\n")
                                    }
                                    message = sb.toString()
                                } else {
                                    message = getString(R.string.msg_no_matches)
                                }
                            }

                            NBiometricOperation.VERIFY -> {
                                if (status == NBiometricStatus.OK) {
                                    val sb = StringBuilder()
                                    message = getString(R.string.msg_verification_succeeded)
                                } else {
                                    message = getString(R.string.msg_verification_failed, status.toString())
                                }
                            }

                            else -> {
                                throw AssertionError("Invalid NBiometricOperation")
                            }
                        }
                    }
                    showInfo(message)
                } catch (e: Exception) {
                    Log.e(TAG, e.message, e)
                }
            }

            override fun failed(throwable: Throwable, nBiometricOperation: NBiometricOperation?) {
            }
        }

    private fun ifAllPermissionsGranted(results: IntArray): Boolean {
        var finalResult = true
        for (permissionResult in results) {
            finalResult = finalResult and (permissionResult == PackageManager.PERMISSION_GRANTED)
            if (!finalResult) break
        }
        return finalResult
    }

    // ===========================================================
    // Protected methods
    // ===========================================================
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NCore.setContext(this)
        setContentView(R.layout.multi_modal_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mFaces = ArrayList<NLRecord>()
        mFingers = ArrayList<NFRecord>()
        mIris = ArrayList<NERecord>()
        mVoice = ArrayList<NSRecord>()

        mFaceCounter = findViewById(R.id.face_counter) as TextView?
        mFingerCounter = findViewById(R.id.finger_counter) as TextView?
        mIrisCounter = findViewById(R.id.iris_counter) as TextView?
        mVoiceCounter = findViewById(R.id.voice_counter) as TextView?

        mSubjectId = findViewById(R.id.subject_id) as EditText?
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val imageFace = findViewById(R.id.face) as ImageView
        imageFace.setOnClickListener {
            val faceActivity: Intent = Intent(
                this@MultiModalActivity,
                FaceActivity::class.java
            )
            startActivityForResult(faceActivity, MODALITY_CODE_FACE)
        }

        val imageFinger = findViewById(R.id.finger) as ImageView
        imageFinger.setOnClickListener {
            val fingerActivity: Intent = Intent(
                this@MultiModalActivity,
                FingerActivity::class.java
            )
            startActivityForResult(fingerActivity, MODALITY_CODE_FINGER)
        }

        /*ImageView imageIris = (ImageView) findViewById(R.id.iris);
 imageIris.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         Intent irisActivity = new Intent(MultiModalActivity.this, com.reactnativewithoutexpo.multibiometric.multimodal.IrisActivity.class);
         startActivityForResult(irisActivity, MODALITY_CODE_IRIS);
     }
 });*/

        /*ImageView imageVoice = (ImageView) findViewById(R.id.voice);
		imageVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent voiceActivity = new Intent(MultiModalActivity.this, com.reactnativewithoutexpo.multibiometric.multimodal.VoiceActivity.class);
				startActivityForResult(voiceActivity, MODALITY_CODE_VOICE);
			}
		});*/
        val enrollSubject = findViewById(R.id.multimodal_button_enroll) as Button
        enrollSubject.setOnClickListener {
            if (mSubjectId?.getText().toString() === "" || mSubjectId.getText().toString().isEmpty()) {
                showError("Missing subject ID")
            } else {
                val subject: NSubject? = createSubjectFromRecords(mFaces!!, mFingers!!, mIris!!, mVoice!!)
                if (subject != null) {
                    subject.setId(mSubjectId.getText().toString())
                    val operation: NBiometricOperation =
                        if ((MultimodalPreferences.isCheckForDuplicates() && (ConnectionPreferences.getConnectionType(
                                NCore.getContext()
                            ) === ConnectionPreferences.ConnectionType.SQLITE))
                        ) NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK else NBiometricOperation.ENROLL
                    val task: NBiometricTask =
                        Model.getInstance().getClient().createTask(EnumSet.of<E>(operation), subject)
                    Model.getInstance().getClient().performTask(task, operation, completionHandler)
                } else {
                    showError("Empty subject")
                }
            }
        }
        val identifySubject = findViewById(R.id.multimodal_button_identify) as Button
        identifySubject.setOnClickListener {
            val subject: NSubject? = createSubjectFromRecords(mFaces!!, mFingers!!, mIris!!, mVoice!!)
            if (subject != null) {
                val task: NBiometricTask = Model.getInstance().getClient()
                    .createTask(EnumSet.of<E>(NBiometricOperation.IDENTIFY), subject)
                Model.getInstance().getClient().performTask(task, NBiometricOperation.IDENTIFY, completionHandler)
            } else {
                showError("Empty subject")
            }
        }
        val clearSubject = findViewById(R.id.multimodal_button_new) as Button
        clearSubject.setOnClickListener {
            mFaces!!.clear()
            mFingers!!.clear()
            mIris!!.clear()
            mVoice!!.clear()
            mSubjectId.setText("")
            updateRecordCount(mFaces, mFingers, mIris, mVoice)
        }
        val mVerify = findViewById(R.id.multimodal_button_verify) as Button
        mVerify.setOnClickListener { verify() }

        updateRecordCount(mFaces, mFingers, mIris, mVoice)

        val neededPermissions = notGrantedPermissions
        if (neededPermissions.size == 0) {
            InitializationTask().execute()
        } else {
            requestPermissions(neededPermissions)
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                MODALITY_CODE_FACE -> {
                    if (data != null) {
                        val b = data.extras
                        val template = b!!.getByteArray("face")
                        val nLTemplate: NLTemplate = NLTemplate(NBuffer(template))
                        if (nLTemplate != null) {
                            for (rec in nLTemplate.getRecords()) {
                                mFaces!!.add(rec)
                            }
                        }
                    }
                }

                MODALITY_CODE_FINGER -> {
                    if (data != null) {
                        val b = data.extras
                        val template = b!!.getByteArray("finger")
                        val nFTemplate: NFTemplate = NFTemplate(NBuffer(template))
                        if (nFTemplate != null) {
                            for (rec in nFTemplate.getRecords()) {
                                mFingers!!.add(rec)
                            }
                        }
                    }
                }

                MODALITY_CODE_IRIS -> {
                    if (data != null) {
                        val b = data.extras
                        val template = b!!.getByteArray("iris")
                        val nETemplate: NETemplate = NETemplate(NBuffer(template))

                        if (nETemplate != null) {
                            for (rec in nETemplate.getRecords()) {
                                mIris!!.add(rec)
                            }
                        }
                    }
                }

                MODALITY_CODE_VOICE -> {
                    if (data != null) {
                        val b = data.extras
                        val template = b!!.getByteArray("voice")
                        val nSTEmplate: NSTemplate = NSTemplate(NBuffer(template))
                        if (nSTEmplate != null) {
                            for (rec in nSTEmplate.getRecords()) {
                                mVoice!!.add(rec)
                            }
                        }
                    }
                }

                else -> {
                    throw AssertionError("Unrecognised request code")
                }
            }
        }
        updateRecordCount(mFaces, mFingers, mIris, mVoice)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.multimodal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear_db -> {
                AlertDialog.Builder(this)
                    .setTitle("Clear database")
                    .setMessage("Are you sure you want to clear database?")
                    .setPositiveButton("Clear", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Model.getInstance().getClient().clear()
                        }
                    }).setNegativeButton("Cancel", null).show()
            }

            R.id.action_view_db -> {
                showDatabase()
            }

            R.id.action_activation -> {
                val activation: Intent = Intent(this, ActivationActivity::class.java)
                val params = Bundle()
                params.putStringArrayList(
                    ActivationActivity.LICENSES, ArrayList<String>(
                        allComponentsInternal
                    )
                )
                activation.putExtras(params)
                startActivity(activation)
            }

            R.id.action_preferences -> {
                startActivity(Intent(this, MultimodalPreferences::class.java))
            }

            R.id.action_connection -> {
                startActivity(Intent(this, ConnectionPreferences::class.java))
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (grantResults.size > 0) {
                    // Check if all permissions granted

                    if (!ifAllPermissionsGranted(grantResults)) {
                        showDialogOK(
                            WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS,
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            Log.w(TAG, WARNING_NOT_ALL_GRANTED)
                                            var i = 0
                                            while (i < permissions.size) {
                                                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                                    Log.w(TAG, permissions[i] + ": PERMISSION_DENIED")
                                                }
                                                i++
                                            }
                                            InitializationTask().execute()
                                        }

                                        DialogInterface.BUTTON_NEGATIVE -> requestPermissions(permissions)
                                        else -> throw AssertionError("Unrecognised permission dialog parameter value")
                                    }
                                }
                            })
                    } else {
                        Log.i(TAG, MESSAGE_ALL_PERMISSIONS_GRANTED)
                        InitializationTask().execute()
                    }
                }
            }
        }
    }

    internal inner class InitializationTask : AsyncTask<Any?, Boolean?, Boolean?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(R.string.msg_initializing)
        }

        override fun doInBackground(vararg params: Any): Boolean {
            showProgress(R.string.msg_obtaining_licenses)
            try {
                LicensingManager.getInstance().obtain(
                    this@MultiModalActivity,
                    additionalComponentsInternal
                )
                if (LicensingManager.getInstance().obtain(
                        this@MultiModalActivity,
                        mandatoryComponentsInternal
                    )
                ) {
                    showToast(R.string.msg_licenses_obtained)
                } else {
                    showToast(R.string.msg_licenses_partially_obtained)
                }
            } catch (e: Exception) {
                showError(e.message, false)
            }
            showProgress(R.string.msg_initializing_client)

            try {
                val client: NBiometricClient = Model.getInstance().getClient()
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                return false
            }
            return true
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            hideProgress()
        }
    }

    companion object {
        // ===========================================================
        // private static fields
        // ===========================================================
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        private const val WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS =
            "Do you wish to proceed without granting all permissions?"
        private const val WARNING_NOT_ALL_GRANTED = "Some permissions are not granted."
        private const val MESSAGE_ALL_PERMISSIONS_GRANTED = "All permissions granted"

        private val TAG: String = MultiModalActivity::class.java.simpleName

        private val mandatoryComponentsInternal: MutableList<String>
            // ===========================================================
            get() {
                val components: MutableList<String> = ArrayList()
                for (component in FaceActivity.Companion.mandatoryComponents()) {
                    if (!components.contains(component)) {
                        components.add(component)
                    }
                }
                for (component in FingerActivity.Companion.mandatoryComponents()) {
                    if (!components.contains(component)) {
                        components.add(component)
                    }
                }
                /*for (String component : IrisActivity.mandatoryComponents()) {
        if (!components.contains(component)) {
            components.add(component);
        }
    }
    for (String component : VoiceActivity.mandatoryComponents()) {
        if (!components.contains(component)) {
            components.add(component);
        }
    }*/
                return components
            }

        private val additionalComponentsInternal: List<String>
            get() {
                val components: MutableList<String> = ArrayList()
                for (component in FaceActivity.Companion.additionalComponents()) {
                    if (!components.contains(component)) {
                        components.add(component)
                    }
                }
                for (component in FingerActivity.Companion.additionalComponents()) {
                    if (!components.contains(component)) {
                        components.add(component)
                    }
                }
                /*		for (String component : IrisActivity.additionalComponents()) {
                if (!components.contains(component)) {
                    components.add(component);
                }
            }
            for (String component : VoiceActivity.additionalComponents()) {
                if (!components.contains(component)) {
                    components.add(component);
                }
            }*/
                return components
            }

        private val requiredPermissions: List<String>
            get() {
                val permissions: MutableList<String> = ArrayList()
                permissions.add(Manifest.permission.INTERNET)
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE)
                permissions.add(Manifest.permission.CAMERA)
                permissions.add(Manifest.permission.RECORD_AUDIO)
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (Build.VERSION.SDK_INT < 23) {
                    permissions.add(Manifest.permission.WRITE_SETTINGS)
                }
                return permissions
            }

        val allComponentsInternal: List<String>
            get() {
                val combinedComponents =
                    mandatoryComponentsInternal
                combinedComponents.addAll(additionalComponentsInternal)
                return combinedComponents
            }

        // ===========================================================
        // Public methods
        // ===========================================================
        fun toLowerCase(string: String): String {
            val sb = StringBuilder()
            sb.append(string.substring(0, 1).uppercase(Locale.getDefault()))
            sb.append(string.substring(1).lowercase(Locale.getDefault()))
            return sb.toString().replace("_".toRegex(), " ")
        }
    }
}
