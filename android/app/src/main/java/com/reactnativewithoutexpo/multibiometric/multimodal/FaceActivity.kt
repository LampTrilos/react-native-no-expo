package com.reactnativewithoutexpo.multibiometric.multimodal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.neurotec.biometrics.*
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.biometrics.standards.BDIFStandard
import com.neurotec.biometrics.standards.FCRecord
import com.neurotec.biometrics.view.NFaceView
import com.neurotec.devices.NCamera
import com.neurotec.devices.NDeviceType
import com.neurotec.images.NImage
import com.neurotec.media.NMediaFormat
import com.neurotec.samples.licensing.LicensingManager
import com.neurotec.samples.util.IOUtils
import com.neurotec.samples.util.NImageUtils
import com.reactnativewithoutexpo.R
import com.reactnativewithoutexpo.multibiometric.Model
import com.reactnativewithoutexpo.multibiometric.preferences.FacePreferences
import com.reactnativewithoutexpo.multibiometric.view.CameraControlsView
import com.reactnativewithoutexpo.multibiometric.view.CameraFormatFragment
import java.io.IOException
import java.util.*

class FaceActivity : BiometricActivity(), CameraControlsView.CameraControlsListener,
    CameraFormatFragment.CameraFormatSelectionListener {
    private enum class Status {
        CAPTURING,
        OPENING_FILE,
        TEMPLATE_CREATED
    }

    // ===========================================================
    // Private fields
    // ===========================================================
    private var mFaceView: NFaceView? = null
    private var controlsView: CameraControlsView? = null

    private var mLicensesObtained = false
    private var mStatus = Status.CAPTURING

    // ===========================================================
    // Private methods
    // ===========================================================
    private fun startCapturing() {
        val subject: NSubject = NSubject()
        val face: NFace = NFace()
        face.addPropertyChangeListener(biometricPropertyChanged)
        val options: EnumSet<NBiometricCaptureOption> =
            EnumSet.of<NBiometricCaptureOption>(NBiometricCaptureOption.MANUAL)
        if (FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this)) {
            options.add(NBiometricCaptureOption.STREAM)
            mFaceView.setShowIcaoArrows(FacePreferences.isShowIcaoWarnings(this))
            mFaceView.setShowIcaoTextWarnings(FacePreferences.isShowIcaoTextWarnings(this))
        }
        if (FacePreferences.isUseLiveness(this)) {
            if (!options.contains(NBiometricCaptureOption.STREAM)) {
                options.add(NBiometricCaptureOption.STREAM)
            }
        }
        face.setCaptureOptions(options)
        mFaceView.setFace(face)
        subject.getFaces().add(face)
        capture(
            subject,
            if ((FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this))) EnumSet.of<NBiometricOperation>(
                NBiometricOperation.ASSESS_QUALITY
            ) else null
        )
    }

    private fun setCameraControlsVisible(value: Boolean) {
        runOnUiThread(Runnable { controlsView.setVisibility(if (value) View.VISIBLE else View.GONE) })
    }

    private fun createSubjectFromImage(uri: Uri?): NSubject? {
        var subject: NSubject? = null
        try {
            val image: NImage = NImageUtils.fromUri(this, uri)
            subject = NSubject()
            val face: NFace = NFace()
            face.setImage(image)
            subject.getFaces().add(face)
        } catch (e: Exception) {
            Log.i(TAG, "Failed to load file as NImage")
        }
        return subject
    }

    private fun createSubjectFromFCRecord(uri: Uri?): NSubject? {
        var subject: NSubject? = null
        try {
            val fcRecord: FCRecord = FCRecord(IOUtils.toByteBuffer(this, uri), BDIFStandard.ISO)
            subject = NSubject()
            for (img in fcRecord.getFaceImages()) {
                val face: NFace = NFace()
                face.setImage(img.toNImage())
                subject.getFaces().add(face)
            }
        } catch (e: Exception) {
            Log.i(TAG, "Failed to load file as FCRecord")
        }
        return subject
    }

    private fun createSubjectFromMemory(uri: Uri?): NSubject? {
        var subject: NSubject? = null
        try {
            subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri))
        } catch (e: IOException) {
            Log.e(TAG, e.message, e)
        }
        return subject
    }

    // ===========================================================
    // Protected methods
    // ===========================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            PreferenceManager.setDefaultValues(this, R.xml.face_preferences, false)
            val layout: LinearLayout = findViewById(R.id.multimodal_biometric_layout) as LinearLayout

            controlsView = CameraControlsView(this, this)
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

            controlsView.setLayoutParams(params)
            layout.addView(controlsView)

            mFaceView = NFaceView(this)
            mFaceView.setShowAge(true)
            layout.addView(mFaceView)

            val backButton = findViewById(R.id.multimodal_button_retry) as Button
            backButton.setOnClickListener {
                startCapturing()
                onBack()
                mStatus = Status.CAPTURING
            }

            val add = findViewById(R.id.multimodal_button_add) as Button
            add.setOnClickListener {
                val intent = Intent()
                val b = Bundle()
                val nLTemplate = subject.template.faces.save().toByteArray()
                b.putByteArray(BiometricActivity.Companion.RECORD_REQUEST_FACE, nLTemplate.copyOf(nLTemplate.size))
                intent.putExtras(b)
                setResult(RESULT_OK, intent)
                finish()
            }
        } catch (e: Exception) {
            showError(e)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mLicensesObtained && mStatus == Status.CAPTURING) {
            startCapturing()
        }
    }

    override val additionalComponents: List<String>
        get() = additionalComponents()

    override val mandatoryComponents: List<String>
        get() = mandatoryComponents()

    override val preferences: Class<*>?
        get() = FacePreferences::class.java

    override fun updatePreferences(client: NBiometricClient?) {
        if (client != null) {
            FacePreferences.updateClient(client, this)
        }
    }

    override val isCheckForDuplicates: Boolean
        get() = FacePreferences.isCheckForDuplicates(this)

    override fun onLicensesObtained() {
        mLicensesObtained = true
        startCapturing()
    }

    override fun onStartCapturing() {
        stop()
    }

    @Throws(Exception::class)
    override fun onFileSelected(uri: Uri?) {
        mStatus = Status.OPENING_FILE

        var subject: NSubject? = createSubjectFromImage(uri)

        if (subject == null) {
            subject = createSubjectFromFCRecord(uri)
        }

        if (subject == null) {
            subject = createSubjectFromMemory(uri)
        }

        if (subject != null) {
            if (!subject.getFaces().isEmpty()) {
                mFaceView.setFace(subject.getFaces().get(0))
            }
            extract(subject)
        } else {
            mStatus = Status.CAPTURING
            showInfo("File did not contain valid information for subject")
        }
    }

    override fun onOperationStarted(operation: NBiometricOperation) {
        super.onOperationStarted(operation)
        if (operation == NBiometricOperation.CAPTURE) {
            mStatus = Status.CAPTURING
            setCameraControlsVisible(true)
        }
    }

    override fun onOperationCompleted(operation: NBiometricOperation, task: NBiometricTask?) {
        super.onOperationCompleted(operation, task)
        if (task != null && task.getStatus() == NBiometricStatus.OK && operation == NBiometricOperation.CREATE_TEMPLATE) {
            mStatus = Status.TEMPLATE_CREATED
            setCameraControlsVisible(false)
        }

        if (task == null || (operation == NBiometricOperation.CREATE_TEMPLATE && task.getStatus() != NBiometricStatus.OK && task.getStatus() != NBiometricStatus.CANCELED && task.getStatus() != NBiometricStatus.OPERATION_NOT_ACTIVATED)) {
            if (!mAppIsGoingToBackground) {
                startCapturing()
            }
        }
    }

    override val isStopSupported: Boolean
        get() = false

    // ===========================================================
    // 	Public methods
    // ===========================================================
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCameraFormatSelected(format: NMediaFormat?) {
        Thread {
            val camera: NCamera = Model.getInstance().getClient().getFaceCaptureDevice()
            if (camera != null) {
                camera.setCurrentFormat(format)
            }
        }.start()
    }

    override fun onSwitchCamera() {
        val currentCamera: NCamera = client.faceCaptureDevice
        for (device in client.deviceManager.devices) {
            if (device.deviceType.contains(NDeviceType.CAMERA)) {
                if (device != currentCamera && currentCamera.isCapturing()) {
                    cancel()
                    client.faceCaptureDevice = device as NCamera
                    startCapturing()
                    break
                }
            }
        }
    }

    override fun onChangeFormat() {
        CameraFormatFragment.newInstance().show(getFragmentManager(), "formats")
    }

    companion object {
        // ===========================================================
        // Private static fields
        // ===========================================================
        private const val TAG = "FaceActivity"
        const val modalityAssetDirectory: String = "faces"
            get() = Companion.field

        fun mandatoryComponents(): List<String> {
            return Arrays.asList<String>(
                LicensingManager.LICENSE_DEVICES_CAMERAS,
                LicensingManager.LICENSE_FACE_DETECTION,
                LicensingManager.LICENSE_FACE_EXTRACTION,
                LicensingManager.LICENSE_FACE_MATCHING
            )
        }

        fun additionalComponents(): List<String> {
            return Arrays.asList<String>(
                LicensingManager.LICENSE_FACE_STANDARDS,
                LicensingManager.LICENSE_FACE_MATCHING_FAST,
                LicensingManager.LICENSE_FACE_SEGMENTS_DETECTION
            )
        }
    }
}
