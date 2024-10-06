package com.reactnativewithoutexpo.multibiometric.preferences

//import android.support.v4.app.NavUtils;
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import androidx.core.app.NavUtils
import com.neurotec.biometrics.NLivenessMode
import com.neurotec.biometrics.NMatchingSpeed
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.samples.view.BasePreferenceFragment
import com.reactnativewithoutexpo.R

class FacePreferences : PreferenceActivity() {
    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        getActionBar()?.setDisplayHomeAsUpEnabled(false)
        getFragmentManager().beginTransaction().replace(android.R.id.content, FacePreferencesFragment()).commit()
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

    class FacePreferencesFragment : BasePreferenceFragment() {
        // ===========================================================
        // Public methods
        // ===========================================================
        override fun onCreate(savedInstanceState: android.os.Bundle?) {
            super.onCreate(savedInstanceState)
            //			getPreferenceManager().setSharedPreferencesName("my_preferences");
//			getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.face_preferences)
        }

        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen, preference: Preference): Boolean {
            if (preference.getKey() == SET_DEFAULT_PREFERENCES) {
                //TODO: Check for better UI update method
                preferenceScreen.getEditor().clear().commit()
                getFragmentManager().beginTransaction().replace(android.R.id.content, FacePreferencesFragment())
                    .commit()
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }

    companion object {
        // ===========================================================
        // Public static fields
        // ===========================================================
        const val FACE_ENROLLMENT_CHECK_FOR_DUPLICATES: String = "face_enrollment_check_for_duplicates"

        const val MATCHING_SPEED: String = "face_matching_speed"
        const val MATCHING_THRESHOLD: String = "face_matching_threshold"

        const val MIN_IOD: String = "face_min_iod"

        const val CONFIDENCE_THRESHOLD: String = "face_confidence_threshold"
        const val QUALITY_THRESHOLD: String = "face_quality_threshold"

        const val MAXIMAL_YAW: String = "face_maximal_yaw"
        const val MAXIMAL_ROLL: String = "face_maximal_roll"

        const val DETECT_ALL_FEATURE_POINTS: String = "face_detect_all_feature_points"
        const val DETERMINE_GENDER: String = "face_determine_gender"
        const val DETECT_PROPERTIES: String = "face_detect_properties"
        const val RECOGNIZE_EXPRESSION: String = "face_recognize_expression"
        const val RECOGNIZE_EMOTION: String = "face_recognize_emotion"

        const val LIVENESS_MODE: String = "liveness_mode"
        const val LIVENESS_THRESHOLD: String = "liveness_threshold"

        const val DETERMINE_AGE: String = "face_determine_age"

        const val CREATE_THUMBNAIL: String = "face_create_thumbnail"
        const val THUMBNAIL_WIDTH: String = "face_thumbnail_width"
        const val ICAO_SHOW_WARNINGS: String = "face_icao_show_warnings"
        const val ICAO_SHOW_WARNING_TEXT: String = "face_icao_show_warning_text"

        //	public static final String CAPTURE_DEVICE = "face_capture_device";
        const val SET_DEFAULT_PREFERENCES: String = "face_set_default_preferences"

        // ===========================================================
        // Public static methods
        // ===========================================================
        fun updateClient(client: NBiometricClient, context: android.content.Context?) {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            client.setFacesMatchingSpeed(
                NMatchingSpeed.get(
                    preferences.getString(
                        MATCHING_SPEED,
                        NMatchingSpeed.LOW.getValue().toString()
                    ).toInt()
                )
            )
            client.setMatchingThreshold(preferences.getString(MATCHING_THRESHOLD, "48").toInt())

            client.setFacesMinimalInterOcularDistance(preferences.getInt(MIN_IOD, 40))
            client.setFacesConfidenceThreshold(preferences.getInt(CONFIDENCE_THRESHOLD, 50).toByte())
            client.setFacesQualityThreshold(preferences.getInt(QUALITY_THRESHOLD, 50).toByte())

            client.setFacesMaximalYaw(preferences.getInt(MAXIMAL_YAW, 15).toFloat())
            client.setFacesMaximalRoll(preferences.getInt(MAXIMAL_ROLL, 15).toFloat())

            client.setFacesDetectAllFeaturePoints(preferences.getBoolean(DETECT_ALL_FEATURE_POINTS, false))
            client.setFacesDetermineGender(preferences.getBoolean(DETERMINE_GENDER, false))
            client.setFacesDetectProperties(preferences.getBoolean(DETECT_PROPERTIES, false))
            client.setFacesRecognizeExpression(preferences.getBoolean(RECOGNIZE_EXPRESSION, false))
            client.setFacesRecognizeEmotion(preferences.getBoolean(RECOGNIZE_EMOTION, false))

            client.setFacesLivenessMode(
                NLivenessMode.get(
                    preferences.getString(
                        LIVENESS_MODE,
                        NLivenessMode.NONE.getValue().toString()
                    ).toInt()
                )
            )
            client.setFacesLivenessThreshold(preferences.getInt(LIVENESS_THRESHOLD, 50).toByte())

            client.setFacesDetermineAge(!isUseLiveness(context) && preferences.getBoolean(DETERMINE_AGE, false))

            client.setFacesCreateThumbnailImage(preferences.getBoolean(CREATE_THUMBNAIL, false))
            client.setFacesThumbnailImageWidth(preferences.getInt(THUMBNAIL_WIDTH, 90))
            client.setFacesCheckIcaoCompliance(preferences.getBoolean(ICAO_SHOW_WARNINGS, false))
        }

        fun isShowIcaoTextWarnings(context: android.content.Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ICAO_SHOW_WARNING_TEXT, false)
        }

        fun isShowIcaoWarnings(context: android.content.Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ICAO_SHOW_WARNINGS, false)
        }

        fun isCheckForDuplicates(context: android.content.Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                FACE_ENROLLMENT_CHECK_FOR_DUPLICATES, false
            )
        }

        fun isUseLiveness(context: android.content.Context?): Boolean {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val mode: NLivenessMode = NLivenessMode.get(
                preferences.getString(LIVENESS_MODE, NLivenessMode.NONE.getValue().toString()).toInt()
            )
            return mode != NLivenessMode.NONE
        }
    }
}
