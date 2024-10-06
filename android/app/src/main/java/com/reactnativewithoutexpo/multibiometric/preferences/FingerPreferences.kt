package com.reactnativewithoutexpo.multibiometric.preferences

//import android.support.v4.app.NavUtils;
import android.content.SharedPreferences
import android.preference.ListPreference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import androidx.core.app.NavUtils
import com.neurotec.biometrics.NMatchingSpeed
import com.neurotec.biometrics.NTemplateSize
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.devices.NDeviceType
import com.neurotec.samples.view.BasePreferenceFragment
import com.reactnativewithoutexpo.multibiometric.Model

class FingerPreferences : PreferenceActivity() {
    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        getActionBar().setDisplayHomeAsUpEnabled(false)
        getFragmentManager().beginTransaction().replace(android.R.id.content, FingerPreferencesFragment()).commit()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class FingerPreferencesFragment : BasePreferenceFragment() {
        // ===========================================================
        // Public methods
        // ===========================================================
        override fun onCreate(savedInstanceState: android.os.Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.finger_preferences)
            setDeviceListPreferenceData(findPreference(FINGER_CAPTURING_DEVICE) as ListPreference)
        }

        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen, preference: Preference): Boolean {
            if (preference.getKey() == SET_DEFAULT_PREFERENCES) {
                preferenceScreen.getEditor().clear().commit()
                getFragmentManager().beginTransaction().replace(android.R.id.content, FingerPreferencesFragment())
                    .commit()
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }

        protected fun setDeviceListPreferenceData(listPreference: ListPreference) {
            val deviceList: MutableList<String> = java.util.ArrayList()
            for (device in Model.getInstance().getClient().getDeviceManager().getDevices()) {
                if (device.getDeviceType().contains(NDeviceType.FSCANNER)) {
                    deviceList.add(device.getId())
                }
            }
            if (deviceList.size == 0) {
                deviceList.add("None")
            }

            val cs = deviceList.toTypedArray<CharSequence>()
            listPreference.setEntries(cs)
            listPreference.setEntryValues(cs)
            val preferedDevice: String = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(
                FINGER_CAPTURING_DEVICE, "None"
            ).toString()

            if (deviceList.contains(preferedDevice)) {
                listPreference.setValue(preferedDevice)
            } else {
                listPreference.setValueIndex(0)
            }
        }
    }

    companion object {
        // ===========================================================
        // Public static fields
        // ===========================================================
        const val FINGER_CAPTURING_DEVICE: String = "finger_capturing_device"

        const val MATCHING_SPEED: String = "finger_matching_speed"
        const val MAXIMAL_ROTATION: String = "finger_maximal_rotation"

        const val TEMPLATE_SIZE: String = "finger_template_size"
        const val QUALITY_THRESHOLD: String = "finger_quality_threshold"
        const val FAST_EXTRACTION: String = "finger_fast_extraction"
        const val RETURN_BINARIZED_IMAGE: String = "finger_return_binarized_image"

        const val FINGER_ENROLLMENT_CHECK_FOR_DUPLICATES: String = "finger_enrollment_check_for_duplicates"

        const val SET_DEFAULT_PREFERENCES: String = "finger_set_default_preferences"

        // ===========================================================
        // Public static methods
        // ===========================================================
        fun updateClient(client: NBiometricClient, context: android.content.Context?) {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            client.setFingersMatchingSpeed(
                NMatchingSpeed.get(
                    preferences.getString(
                        MATCHING_SPEED,
                        NMatchingSpeed.LOW.getValue().toString()
                    ).toInt()
                )
            )
            client.setFingersMaximalRotation(preferences.getInt(MAXIMAL_ROTATION, 180).toFloat())

            client.setFingersTemplateSize(
                preferences.getString(
                    TEMPLATE_SIZE,
                    NTemplateSize.SMALL.getValue().toString()
                )?.let {
                    NTemplateSize.get(
                        it.toInt()
                    )
                }
            )
            client.setFingersQualityThreshold(preferences.getInt(QUALITY_THRESHOLD, 39.toByte().toInt()).toByte())
            client.setFingersFastExtraction(preferences.getBoolean(FAST_EXTRACTION, false))
            client.setFingersReturnBinarizedImage(preferences.getBoolean(RETURN_BINARIZED_IMAGE, false))
        }

        fun isCheckForDuplicates(context: android.content.Context?): Boolean {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(FINGER_ENROLLMENT_CHECK_FOR_DUPLICATES, true)
        }

        fun isReturnBinarizedImage(context: android.content.Context?): Boolean {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(RETURN_BINARIZED_IMAGE, false)
        }
    }
}
