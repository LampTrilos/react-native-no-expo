package com.reactnativewithoutexpo.multibiometric.multimodal;

import static com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity.getAdditionalComponentsInternal;
import static com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity.getMandatoryComponentsInternal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.neurotec.biometrics.NBiometricCaptureOption;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.standards.BDIFStandard;
import com.neurotec.biometrics.standards.FCRFaceImage;
import com.neurotec.biometrics.standards.FCRecord;
import com.neurotec.biometrics.view.NFaceView;
import com.neurotec.devices.NCamera;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.images.NImage;
import com.neurotec.licensing.gui.ActivationActivity;
import com.neurotec.media.NMediaFormat;
import com.neurotec.samples.app.BaseActivity;
import com.neurotec.samples.licensing.LicensingManager;
import com.reactnativewithoutexpo.multibiometric.Model;
import com.reactnativewithoutexpo.R;
import com.reactnativewithoutexpo.multibiometric.preferences.FacePreferences;
import com.reactnativewithoutexpo.multibiometric.view.CameraControlsView;
import com.reactnativewithoutexpo.multibiometric.view.CameraControlsView.CameraControlsListener;
import com.reactnativewithoutexpo.multibiometric.view.CameraFormatFragment;
import com.reactnativewithoutexpo.multibiometric.view.CameraFormatFragment.CameraFormatSelectionListener;
import com.neurotec.samples.util.IOUtils;
import com.neurotec.samples.util.NImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public final class FaceActivity extends BiometricActivity implements CameraControlsListener, CameraFormatSelectionListener, ActivityCompat.OnRequestPermissionsResultCallback {

	private static final int REQUEST_CAMERA_PERMISSION = 1;
	private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

	private enum Status {
		CAPTURING,
		OPENING_FILE,
		TEMPLATE_CREATED
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = "FaceActivity";
	private static final String MODALITY_ASSET_DIRECTORY = "faces";

	// ===========================================================
	// Private fields
	// ===========================================================

	private NFaceView mFaceView;
	private CameraControlsView controlsView;

	private boolean mLicensesObtained = false;
	private Status mStatus = Status.CAPTURING;


	//Experiment to get license when bypassing Multimodal Activity
	private static List<String> getRequiredPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add(Manifest.permission.INTERNET);
		permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
		permissions.add(Manifest.permission.CAMERA);
		permissions.add(Manifest.permission.RECORD_AUDIO);
		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return permissions;
	}
	private String[] getNotGrantedPermissions() {
		List<String> neededPermissions = new ArrayList<String>();

		for (String permission : getRequiredPermissions()) {
			//if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				neededPermissions.add(permission);
			//}
		}
		return neededPermissions.toArray(new String[neededPermissions.size()]);
	}


	private void requestPermissions(String[] permissions) {
		ActivityCompat.requestPermissions(this, permissions,REQUEST_ID_MULTIPLE_PERMISSIONS);
	}
	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	//At the end of onCreate, we run the InitializationTask, which initializes the user permissions, but most importantly the Client which controls the various devices of the tablet
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Experiment to get the activation Licenses when skipping the Multimodal screen that normally handles it
		//getLicensesAndPermissions();

		try {
			PreferenceManager.setDefaultValues(this, R.xml.face_preferences, false);
			LinearLayout layout = (LinearLayout) findViewById(R.id.multimodal_biometric_layout);
			controlsView = new CameraControlsView(this, this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			controlsView.setLayoutParams(params);
			layout.addView(controlsView);
			mFaceView = new NFaceView(this);
			mFaceView.setShowAge(true);
			layout.addView(mFaceView);


			//Retry button
			Button backButton = (Button) findViewById(R.id.multimodal_button_retry);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startCapturing();
					onBack();
					mStatus = Status.CAPTURING;
				}
			});

			//Save button
			Button add = (Button) findViewById(R.id.multimodal_button_add);
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					//Could this be the bytes of the image file?
					byte[] nLTemplate = subject.getTemplate().getFaces().save().toByteArray();
					b.putByteArray(RECORD_REQUEST_FACE , Arrays.copyOf(nLTemplate, nLTemplate.length));
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} catch (Exception e) {
			showError(e);
		}
		//On create, we initialize the client(devices etc..) and the permissions
		new InitializationTask().execute();
    }

	//On create, we initialize the client(devices etc..) and the permissions
	final class InitializationTask extends AsyncTask<Object, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(R.string.msg_initializing);
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			showProgress(R.string.msg_obtaining_licenses);
			try {
				LicensingManager.getInstance().obtain(FaceActivity.this, getAdditionalComponentsInternal());
				if (LicensingManager.getInstance().obtain(FaceActivity.this, getMandatoryComponentsInternal())) {
					showToast(R.string.msg_licenses_obtained);
				} else {
					showToast(R.string.msg_licenses_partially_obtained);
				}
			} catch (Exception e) {
				showError(e.getMessage(), false);
			}
			showProgress(R.string.msg_initializing_client);

			try {
				//Client object is inherited from BiometricActivity, the Model class is final and the client object is static
				//This is the first time that it is called, so theoretically
				//NBiometricClient client = Model.getInstance().getClient();
				client = Model.getInstance().getClient();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			hideProgress();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		if (mLicensesObtained && mStatus == Status.CAPTURING) {
			startCapturing();
		}
	}


	// ===========================================================
	// Private methods
	// ===========================================================

	private void startCapturing() {
		//Face Preferences are changed in the file /preferences/FacePreferences//
		//Subject is also an object in BiometricActivity
		NSubject subject = new NSubject();

		NFace face = new NFace();
		face.addPropertyChangeListener(biometricPropertyChanged);
		//Experiment - changed to stream so it auto takes a photo that meets the criteria
		//EnumSet<NBiometricCaptureOption> options = EnumSet.of(NBiometricCaptureOption.MANUAL);
		EnumSet<NBiometricCaptureOption> options = EnumSet.of(NBiometricCaptureOption.STREAM);

		if (FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this)) {
			options.add(NBiometricCaptureOption.STREAM);
			mFaceView.setShowIcaoArrows(FacePreferences.isShowIcaoWarnings(this));
			mFaceView.setShowIcaoTextWarnings(FacePreferences.isShowIcaoTextWarnings(this));
//			mFaceView.setShowIcaoArrows(true);
//			mFaceView.setShowIcaoTextWarnings(true);
		}
		if (FacePreferences.isUseLiveness(this)) {
			if (!options.contains(NBiometricCaptureOption.STREAM)) {
				options.add(NBiometricCaptureOption.STREAM);
			}
		}
		face.setCaptureOptions(options);
		mFaceView.setFace(face);
		subject.getFaces().add(face);
		try {
			//We use sleep because the device needs a little more time to initialize
			Thread.sleep(1300);
			//Explicitly set the camera to the good one, 0 is Microphone, 1 is the bad camera, 3 does not exist
			client.setFaceCaptureDevice((NCamera) client.getDeviceManager().getDevices().get(2));
			//Capture function is declared in BiometricActivity
		capture(subject, (FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this)) ? EnumSet.of(NBiometricOperation.ASSESS_QUALITY) : null);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void setCameraControlsVisible(final boolean value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				controlsView.setVisibility(value ? View.VISIBLE : View.GONE);
			}
		});
	}

//	private NSubject createSubjectFromImage(Uri uri) {
//		NSubject subject = null;
//		try {
//			NImage image = NImageUtils.fromUri(this, uri);
//			subject = new NSubject();
//			NFace face = new NFace();
//			face.setImage(image);
//			subject.getFaces().add(face);
//		} catch (Exception e){
//			Log.i(TAG, "Failed to load file as NImage");
//		}
//		return subject;
//	}

	private NSubject createSubjectFromFCRecord(Uri uri) {
		NSubject subject = null;
		try {
			FCRecord fcRecord = new FCRecord(IOUtils.toByteBuffer(this, uri), BDIFStandard.ISO);
			subject = new NSubject();
			for (FCRFaceImage img : fcRecord.getFaceImages()) {
				NFace face = new NFace();
				face.setImage(img.toNImage());
				subject.getFaces().add(face);
			}
		} catch (Exception e){
			Log.i(TAG, "Failed to load file as FCRecord");
		}
		return subject;
	}

	private NSubject createSubjectFromMemory(Uri uri) {
		NSubject subject = null;
		try {
			subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return subject;
	}



	@Override
	protected List<String> getAdditionalComponents() {
		return additionalComponents();
	}

	@Override
	protected List<String> getMandatoryComponents() {
		return mandatoryComponents();
	}

	@Override
	protected Class<?> getPreferences() {
		return FacePreferences.class;
	}

	@Override
	protected void updatePreferences(NBiometricClient client) {
		FacePreferences.updateClient(client, this);
	}

	@Override
	protected boolean isCheckForDuplicates() {
		return FacePreferences.isCheckForDuplicates(this);
	}

	@Override
	protected String getModalityAssetDirectory() {
		return MODALITY_ASSET_DIRECTORY;
	}

	@Override
	protected void onLicensesObtained() {
		mLicensesObtained = true;
		startCapturing();
	}

	protected void onStartCapturing() {
		stop();
	}

//	@Override
//	protected void onFileSelected(Uri uri) throws Exception {
//		mStatus = Status.OPENING_FILE;
//
//		NSubject subject = createSubjectFromImage(uri);
//
//		if (subject == null) {
//			subject = createSubjectFromFCRecord(uri);
//		}
//
//		if (subject == null) {
//			subject = createSubjectFromMemory(uri);
//		}
//
//		if (subject != null) {
//			if (!subject.getFaces().isEmpty()) {
//				mFaceView.setFace(subject.getFaces().get(0));
//			}
//			extract(subject);
//		} else {
//			mStatus = Status.CAPTURING;
//			showInfo("File did not contain valid information for subject");
//		}
//	}

	@Override
	protected void onOperationStarted(NBiometricOperation operation) {
		super.onOperationStarted(operation);
		if (operation == NBiometricOperation.CAPTURE) {
			mStatus = Status.CAPTURING;
			setCameraControlsVisible(true);
		}
	}

	@Override
	protected void onOperationCompleted(NBiometricOperation operation, NBiometricTask task) {
		super.onOperationCompleted(operation, task);
		//Experiment because 'Operation is not allowed
		if (task != null && task.getStatus() == NBiometricStatus.OK && operation == NBiometricOperation.CREATE_TEMPLATE) {
		//if (task != null && operation == NBiometricOperation.CREATE_TEMPLATE) {
			mStatus = Status.TEMPLATE_CREATED;
			setCameraControlsVisible(false);
		}

		if (task == null || (operation == NBiometricOperation.CREATE_TEMPLATE
				&& task.getStatus() != NBiometricStatus.OK
				&& task.getStatus() != NBiometricStatus.CANCELED
				&& task.getStatus() != NBiometricStatus.OPERATION_NOT_ACTIVATED)) {
			if (!mAppIsGoingToBackground) {
				startCapturing();
			}
		}
	}

	@Override
	protected boolean isStopSupported() {
		return false;
	}

	// ===========================================================
	// 	Public methods
	// ===========================================================

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	public void onCameraFormatSelected(final NMediaFormat format) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				NCamera camera = Model.getInstance().getClient().getFaceCaptureDevice();
//				if (camera != null) {
//					camera.setCurrentFormat(format);
//				}
//
//			}
//		}).start();
//	}

	//Τhis method was changed so that it always switches to the 3d device, as it has better quality
	@Override
	public void onSwitchCamera() {
		//NCamera currentCamera = client.getFaceCaptureDevice();
		//for (NDevice device : client.getDeviceManager().getDevices()) {
			//if (device.getDeviceType().contains(NDeviceType.CAMERA)) {
				//Expreriment because it wouldn't change camera when called from  CameraControlsView
				//if (!device.equals(currentCamera) && currentCamera.isCapturing()) {
					cancel();
					//Explicitly set the camera to the good one, 0 is Microphone, 1 is the bad camera, 3 does not exist
				System.out.println("CHANGING CAMERA TO THE BEST OF THE TWO");
				//Experiment
				//client.setFaceCaptureDevice((NCamera) client.getDeviceManager().getDevices().get(2));
				//	client.setFaceCaptureDevice((NCamera) device);
				//	startCapturing();
					//break;
				//}
			//}
		//}
	}

	@Override
	public void onChangeFormat() {
		CameraFormatFragment.newInstance().show(getFragmentManager(), "formats");
	}

	public static List<String> mandatoryComponents() {
		return Arrays.asList(LicensingManager.LICENSE_DEVICES_CAMERAS,
				LicensingManager.LICENSE_FACE_DETECTION,
				LicensingManager.LICENSE_FACE_EXTRACTION
				//There is no Face Matching capability
				//LicensingManager.LICENSE_FACE_MATCHING);
				);
	}

	public static List<String> additionalComponents() {
		return Arrays.asList(LicensingManager.LICENSE_FACE_STANDARDS,
				LicensingManager.LICENSE_FACE_MATCHING_FAST,
				LicensingManager.LICENSE_FACE_SEGMENTS_DETECTION);
	}
}
