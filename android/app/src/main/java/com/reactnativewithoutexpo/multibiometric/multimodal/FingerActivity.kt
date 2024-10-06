package com.reactnativewithoutexpo.multibiometric.multimodal

import android.R
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.facebook.datasource.SuccessfulVoidDataSource.setResult
import com.neurotec.biometrics.NBiometricStatus
import com.neurotec.biometrics.NFPosition
import com.neurotec.biometrics.NFinger
import com.neurotec.biometrics.NSubject
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.biometrics.view.NFingerView
import com.neurotec.biometrics.view.NFingerViewBase
import com.neurotec.devices.NDevice
import com.neurotec.devices.NDeviceType
import com.neurotec.devices.NFScanner
import com.neurotec.images.NImage
import com.neurotec.samples.licensing.LicensingManager
import com.neurotec.samples.util.IOUtils
import com.neurotec.samples.util.NImageUtils
import com.neurotec.samples.util.ResourceUtils
import com.reactnativewithoutex.BiometricActivity
import com.reactnativewithoutexpo.multibiometric.preferences.FingerPreferences
import java.util.*

class FingerActivity : BiometricActivity() {
    // ===========================================================
    // Private fields
    // ===========================================================
    private var mFingerView: NFingerView? = null
    private var mDefaultBitmap: Bitmap? = null
    private var mStatus: TextView? = null
    private var mFingerPositions: MutableMap<String, NFPosition>? = null

    // ===========================================================
    // Private methods
    // ===========================================================
    private fun setFingerPosition() {
        val builderSingle = AlertDialog.Builder(this@FingerActivity)
        builderSingle.setTitle("Select finger possition")
        builderSingle.setCancelable(false)

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this@FingerActivity, R.layout.select_dialog_singlechoice)

        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.UNKNOWN.name))

        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_LITTLE_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_RING_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_MIDDLE_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_INDEX_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_THUMB.name))

        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_LITTLE_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_RING_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_MIDDLE_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_INDEX_FINGER.name))
        arrayAdapter.add(MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_THUMB.name))

        builderSingle.setNegativeButton("Close", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })

        builderSingle.setAdapter(arrayAdapter, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {
                val strName: String = arrayAdapter.getItem(item).toString()
                val element = strName
                subject.template.fingers.records[0].position = mFingerPositions!![element]
            }
        })
        builderSingle.show()
    }

    private val scanner: NFScanner?
        get() {
            var fingerDevice: NDevice? = null
            for (device in client.deviceManager.devices) {
                if (device.deviceType.contains(NDeviceType.FSCANNER)) {
                    if (device.id == PreferenceManager.getDefaultSharedPreferences(this)
                            .getString(FingerPreferences.FINGER_CAPTURING_DEVICE, "None")
                    ) {
                        return device as NFScanner
                    } else if (fingerDevice == null) {
                        fingerDevice = device
                    }
                }
            }
            return fingerDevice as NFScanner?
        }

    private fun createSubjectFromImage(uri: Uri?): NSubject? {
        var subject: NSubject? = null
        try {
            val image: NImage = NImageUtils.fromUri(this, uri)
            subject = NSubject()
            val finger: NFinger = NFinger()
            finger.setImage(image)
            subject.getFingers().add(finger)
        } catch (e: Exception) {
            Log.i(TAG, "Failed to load file as NImage")
        }
        return subject
    }

    private fun createSubjectFromMemory(uri: Uri?): NSubject? {
        var subject: NSubject? = null
        try {
            subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri))
        } catch (e: Exception) {
            Log.i(TAG, "Failed to load finger from file")
        }
        return subject
    }

    // ===========================================================
    // Protected methods
    // ===========================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            PreferenceManager.setDefaultValues(this, R.xml.finger_preferences, false)
            val layout: LinearLayout = (findViewById(R.id.multimodal_biometric_layout) as LinearLayout)

            mFingerPositions = HashMap<String, NFPosition>()
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.UNKNOWN.name)] = NFPosition.UNKNOWN

            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_LITTLE_FINGER.name)] =
                NFPosition.LEFT_LITTLE_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_RING_FINGER.name)] =
                NFPosition.LEFT_RING_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_MIDDLE_FINGER.name)] =
                NFPosition.LEFT_MIDDLE_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_INDEX_FINGER.name)] =
                NFPosition.LEFT_INDEX_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.LEFT_THUMB.name)] =
                NFPosition.LEFT_THUMB

            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_LITTLE_FINGER.name)] =
                NFPosition.RIGHT_LITTLE_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_RING_FINGER.name)] =
                NFPosition.RIGHT_RING_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_MIDDLE_FINGER.name)] =
                NFPosition.RIGHT_MIDDLE_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_INDEX_FINGER.name)] =
                NFPosition.RIGHT_INDEX_FINGER
            mFingerPositions!![MultiModalActivity.Companion.toLowerCase(NFPosition.RIGHT_THUMB.name)] =
                NFPosition.RIGHT_THUMB

            mFingerView = NFingerView(this)
            layout.addView(mFingerView)

            mStatus = TextView(this)
            mStatus!!.text = "Status"
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.CENTER
            mStatus!!.layoutParams = params
            layout.addView(mStatus)

            mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_finger)
            if (savedInstanceState == null) {
                val finger: NFinger = NFinger()
                finger.setImage(NImage.fromBitmap(mDefaultBitmap))
                mFingerView.setFinger(finger)
            }
            val add = findViewById(R.id.multimodal_button_add) as Button
            add.text = "Add"
            add.setOnClickListener {
                val intent = Intent()
                val b = Bundle()
                val nFTemplate = subject.template.fingers.save().toByteArray()
                b.putByteArray(BiometricActivity.Companion.RECORD_REQUEST_FINGER, nFTemplate.copyOf(nFTemplate.size))
                intent.putExtras(b)
                setResult(RESULT_OK, intent)
                finish()
            }
            val setPosition = findViewById(R.id.multimodal_button_unbound) as Button
            setPosition.text = "Set position"
            setPosition.visibility = View.VISIBLE
            setPosition.setOnClickListener { setFingerPosition() }
        } catch (e: Exception) {
            showError(e)
        }
    }

    protected override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(BUNDLE_KEY_STATUS, if (TextUtils.isEmpty(mStatus!!.text)) "" else mStatus!!.text.toString())
    }

    override val additionalComponents: List<String>
        get() = additionalComponents()

    override val mandatoryComponents: List<String>
        get() = mandatoryComponents()

    override val preferences: Class<*>?
        get() = FingerPreferences::class.java

    override fun updatePreferences(client: NBiometricClient?) {
        FingerPreferences.updateClient(client, this)
    }

    override val isCheckForDuplicates: Boolean
        get() = FingerPreferences.isCheckForDuplicates(this)

    @Throws(Exception::class)
    override fun onFileSelected(uri: Uri?) {
        var subject: NSubject? = null
        mFingerView?.setShownImage(if (FingerPreferences.isReturnBinarizedImage(this)) NFingerViewBase.ShownImage.RESULT else ShownImage.ORIGINAL)
        subject = createSubjectFromImage(uri)

        if (subject == null) {
            subject = createSubjectFromMemory(uri)
        }

        if (subject != null) {
            if (subject.getFingers() != null && subject.getFingers().get(0) != null) {
                mFingerView.setFinger(subject.getFingers().get(0))
            }
            extract(subject)
        } else {
            showInfo(R.string.msg_failed_to_load_image_or_standard)
        }
    }

    override fun onStartCapturing() {
        val scanner: NFScanner? = scanner
        if (scanner == null) {
            showError(R.string.msg_capturing_device_is_unavailable)
        } else {
            client.fingerScanner = scanner
            val subject: NSubject = NSubject()
            val finger: NFinger = NFinger()
            finger.addPropertyChangeListener(biometricPropertyChanged)
            mFingerView?.setShownImage(if (FingerPreferences.isReturnBinarizedImage(this)) NFingerViewBase.ShownImage.RESULT else ShownImage.ORIGINAL)
            mFingerView?.setFinger(finger)
            subject.getFingers().add(finger)
            capture(subject, null)
        }
    }

    override fun onStatusChanged(value: NBiometricStatus?) {
        runOnUiThread(Runnable {
            mStatus!!.text =
                if (value == null) "" else ResourceUtils.getEnum(this@FingerActivity, value)
        })
    }

    companion object {
        // ===========================================================
        // Private static fields
        // ===========================================================
        private val TAG: String = FingerActivity::class.java.simpleName
        private const val BUNDLE_KEY_STATUS = "status"
        val modalityAssetDirectory: String = "fingers"
            get() = Companion.field

        fun mandatoryComponents(): List<String> {
            return Arrays.asList<String>(
                LicensingManager.LICENSE_FINGER_DETECTION,
                LicensingManager.LICENSE_FINGER_EXTRACTION,
                LicensingManager.LICENSE_FINGER_MATCHING,
                LicensingManager.LICENSE_FINGER_DEVICES_SCANNERS
            )
        }

        fun additionalComponents(): List<String> {
            return Arrays.asList<String>(
                LicensingManager.LICENSE_FINGER_WSQ,
                LicensingManager.LICENSE_FINGER_STANDARDS_FINGER_TEMPLATES,
                LicensingManager.LICENSE_FINGER_STANDARDS_FINGERS
            )
            //			LicensingManager.LICENSE_FINGER_QUALITY_ASSESSMENT,
//			LicensingManager.LICENSE_FINGER_SEGMENTS_DETECTION);
        }
    }
}
