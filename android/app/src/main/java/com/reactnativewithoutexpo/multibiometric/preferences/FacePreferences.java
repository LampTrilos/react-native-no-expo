package com.reactnativewithoutexpo.multibiometric.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
//import android.support.v4.app.NavUtils;
import androidx.core.app.NavUtils;
import android.view.MenuItem;

import com.neurotec.biometrics.NLivenessMode;
import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.reactnativewithoutexpo.R;
import com.neurotec.samples.view.BasePreferenceFragment;

public final class FacePreferences extends PreferenceActivity {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String FACE_ENROLLMENT_CHECK_FOR_DUPLICATES = "face_enrollment_check_for_duplicates";

	public static final String MATCHING_SPEED = "face_matching_speed";
	public static final String MATCHING_THRESHOLD = "face_matching_threshold";

	public static final String MIN_IOD = "face_min_iod";

	public static final String CONFIDENCE_THRESHOLD = "face_confidence_threshold";
	public static final String QUALITY_THRESHOLD = "face_quality_threshold";

	public static final String MAXIMAL_YAW = "face_maximal_yaw";
	public static final String MAXIMAL_ROLL = "face_maximal_roll";

	public static final String DETECT_ALL_FEATURE_POINTS = "face_detect_all_feature_points";
	public static final String DETERMINE_GENDER = "face_determine_gender";
	public static final String DETECT_PROPERTIES = "face_detect_properties";
	public static final String RECOGNIZE_EXPRESSION = "face_recognize_expression";
	public static final String RECOGNIZE_EMOTION = "face_recognize_emotion";

	public static final String LIVENESS_MODE = "liveness_mode";
	public static final String LIVENESS_THRESHOLD = "liveness_threshold";

	public static final String DETERMINE_AGE = "face_determine_age";

	public static final String CREATE_THUMBNAIL = "face_create_thumbnail";
	public static final String THUMBNAIL_WIDTH = "face_thumbnail_width";
	public static final String ICAO_SHOW_WARNINGS = "face_icao_show_warnings";
	public static final String ICAO_SHOW_WARNING_TEXT = "face_icao_show_warning_text";

//	public static final String CAPTURE_DEVICE = "face_capture_device";
	public static final String SET_DEFAULT_PREFERENCES = "face_set_default_preferences";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static void updateClient(NBiometricClient client, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		client.setFacesMatchingSpeed(NMatchingSpeed.get(Integer.valueOf(preferences.getString(MATCHING_SPEED, String.valueOf(NMatchingSpeed.LOW.getValue())))));
		client.setMatchingThreshold(Integer.valueOf(preferences.getString(MATCHING_THRESHOLD, "48")));

		client.setFacesConfidenceThreshold((byte) preferences.getInt(CONFIDENCE_THRESHOLD, 40));
		//Quality threshold
		client.setFacesQualityThreshold((byte) preferences.getInt(QUALITY_THRESHOLD, 30));
		//Anti-2D photo mechanism
		client.setFacesLivenessMode(NLivenessMode.get(Integer.valueOf(preferences.getString(LIVENESS_MODE, String.valueOf(NLivenessMode.NONE.getValue())))));
		//client.setFacesLivenessMode(NLivenessMode.get(Integer.valueOf(preferences.getString(LIVENESS_MODE, String.valueOf(NLivenessMode.ACTIVE.getValue())))));
		client.setFacesLivenessThreshold((byte) preferences.getInt(LIVENESS_THRESHOLD, 50));
		//Distance between the eyes
		client.setFacesMinimalInterOcularDistance(preferences.getInt(MIN_IOD, 40));

		client.setFacesMaximalYaw(Float.valueOf(preferences.getInt(MAXIMAL_YAW, 10)));
		client.setFacesMaximalRoll(Float.valueOf(preferences.getInt(MAXIMAL_ROLL, 10)));

		client.setFacesDetectAllFeaturePoints(preferences.getBoolean(DETECT_ALL_FEATURE_POINTS, false));
		client.setFacesDetermineGender(preferences.getBoolean(DETERMINE_GENDER, true));
		client.setFacesDetectProperties(preferences.getBoolean(DETECT_PROPERTIES, true));
		client.setFacesRecognizeEmotion(preferences.getBoolean(RECOGNIZE_EMOTION, false));
		client.setFacesDetermineAge(!isUseLiveness(context) && preferences.getBoolean(DETERMINE_AGE, true));
		client.setFacesCreateThumbnailImage(preferences.getBoolean(CREATE_THUMBNAIL, false));
		client.setFacesThumbnailImageWidth(preferences.getInt(THUMBNAIL_WIDTH, 90));
		client.setFacesCheckIcaoCompliance(preferences.getBoolean(ICAO_SHOW_WARNINGS, true));
		//My change because this method doesn't exist anymore
		//client.setFacesRecognizeExpression(preferences.getBoolean(RECOGNIZE_EXPRESSION, false));
	}
	
	public static boolean isShowIcaoTextWarnings(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ICAO_SHOW_WARNING_TEXT, true);
	}
	
	public static boolean isShowIcaoWarnings(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ICAO_SHOW_WARNINGS, false);
	}

	public static boolean isCheckForDuplicates(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(FACE_ENROLLMENT_CHECK_FOR_DUPLICATES, false);
	}

	public static boolean isUseLiveness(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		NLivenessMode mode = NLivenessMode.get(Integer.valueOf(preferences.getString(LIVENESS_MODE, String.valueOf(NLivenessMode.NONE.getValue()))));
		return !mode.equals(NLivenessMode.NONE);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new FacePreferencesFragment()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class FacePreferencesFragment extends BasePreferenceFragment {

		// ===========================================================
		// Public methods
		// ===========================================================

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
//			getPreferenceManager().setSharedPreferencesName("my_preferences");
//			getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.face_preferences);
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			if (preference.getKey().equals(SET_DEFAULT_PREFERENCES)) {
				//TODO: Check for better UI update method
				preferenceScreen.getEditor().clear().commit();
				getFragmentManager().beginTransaction().replace(android.R.id.content, new FacePreferencesFragment()).commit();
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}
}
