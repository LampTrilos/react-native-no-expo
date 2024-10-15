package com.reactnativewithoutexpo.multibiometric;

import android.app.Activity;

import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.client.NClusterBiometricConnection;
import com.neurotec.biometrics.client.NMMAbisConnection;
import com.neurotec.lang.NCore;
import com.reactnativewithoutexpo.multibiometric.preferences.ConnectionPreferences;
import com.reactnativewithoutexpo.multibiometric.preferences.FacePreferences;
import com.reactnativewithoutexpo.multibiometric.preferences.FingerPreferences;
import com.neurotec.samples.util.IOUtils;
import com.neurotec.samples.util.ToastManager;

import java.util.EnumSet;

public final class Model extends ReactContextBaseJavaModule {
	private static ReactApplicationContext reactContext;


	// ===========================================================
	// Private static fields
	// ===========================================================

	private static Model sInstance;

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static Model getInstance() {
		synchronized (Model.class) {
			if (sInstance == null) {
				sInstance = new Model();
			}
			return sInstance;
		}
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private NBiometricClient mClient;
	private NSubject mSubject;

	private NSubject[] mSubjects;

	// ===========================================================
	// Private constructor
	// ===========================================================

	private static void updateClientConnection(NBiometricClient client) {
		switch (ConnectionPreferences.getConnectionType(NCore.getContext())) {
			case SQLITE: {
				client.setDatabaseConnectionToSQLite(IOUtils.combinePath(NCore.getContext().getFilesDir().getAbsolutePath(), "BiometricsV50.db"));
				client.getRemoteConnections().clear();
			} break;
			case CLUSTER: {
				client.setLocalOperations(EnumSet.of(NBiometricOperation.DETECT,
						NBiometricOperation.DETECT_SEGMENTS,
						NBiometricOperation.SEGMENT,
						NBiometricOperation.ASSESS_QUALITY,
						NBiometricOperation.CREATE_TEMPLATE));
				int port = ConnectionPreferences.getClusterClientPort(NCore.getContext());;
				int adminPort = ConnectionPreferences.getClusterAdminPort(NCore.getContext());
				String host = ConnectionPreferences.getClusterServerAddress(NCore.getContext());
				client.getRemoteConnections().add(new NClusterBiometricConnection(host, port, adminPort));
			} break;
			case MMABIS: {
				client.setLocalOperations(EnumSet.noneOf(NBiometricOperation.class));
				String address = ConnectionPreferences.getMMABISServerAddress(NCore.getContext());
				String username = ConnectionPreferences.getMMABISUsername(NCore.getContext());
				String password = ConnectionPreferences.getMMABISPassword(NCore.getContext());
				client.getRemoteConnections().add(new NMMAbisConnection(address, username, password));
			} break;
		}
	}

	private Model() {
		super(reactContext);
		mClient = new NBiometricClient();
		updateClientConnection(mClient);
		mClient.setUseDeviceManager(true);
		mClient.setMatchingWithDetails(true);
		mClient.setProperty("Faces.IcaoUnnaturalSkinToneThreshold", 10);
		mClient.setProperty("Faces.IcaoSkinReflectionThreshold", 10);
		mClient.initialize();
		mSubjects = new NSubject[]{};
		mSubject = new NSubject();
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public NBiometricClient getClient() {
		return mClient;
	}

	public NSubject getSubject() {
		return mSubject;
	}

	/**
	 * Subjects contain copy of subject list from biometric client
	 * so that list could be accessible while continuous tasks are being
	 * performed on biometric client like capturing from camera
	 */
	public NSubject[] getSubjects() {
		return mSubjects;
	}

	/**
	 * Subjects contain copy of subject list from biometric client
	 * so that list could be accessible while continuous tasks are being
	 * performed on biometric client like capturing from camera
	 */
	public void setSubjects(NSubject[] subjects) {
		this.mSubjects = subjects;
	}

	public void update() {
		FingerPreferences.updateClient(mClient, NCore.getContext());
		FacePreferences.updateClient(mClient, NCore.getContext());
	}

	@NonNull
    @Override
    public String getName() {
        return "Model";
    }

	public static void setReactContext(ReactApplicationContext context){
		reactContext = context;
	}

	public static ReactApplicationContext getReactContext(){
		return reactContext;
	}
}
