//package com.reactnativewithoutexpo;
//
//import android.util.Log;
//import android.app.Application;
//import android.content.Context;
//import android.content.res.Configuration;
//import androidx.annotation.NonNull;
//
//import com.facebook.react.PackageList;
//import com.facebook.react.ReactApplication;
//import com.facebook.react.ReactInstanceManager;
//import com.facebook.react.ReactNativeHost;
//import com.facebook.react.ReactPackage;
//import com.facebook.soloader.SoLoader;
//
//import expo.modules.ApplicationLifecycleDispatcher;
//import expo.modules.ReactNativeHostWrapper;
//
//import com.facebook.react.bridge.JSIModulePackage;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.List;
//
//import com.neurotec.licensing.NLicenseManager;
//import com.neurotec.licensing.gui.LicensingPreferencesFragment;
//import com.neurotec.samples.util.EnvironmentUtils;
//
//public class BiometricApplication extends Application implements ReactApplication {
//// ===========================================================
//	// Private static fields
//	// ===========================================================
//
//	private static final String TAG = BiometricApplication.class.getSimpleName();
//
//	private final ReactNativeHost mReactNativeHost = new ReactNativeHostWrapper(
//		this,
//		new ReactNativeHost(this) {
//		@Override
//		public boolean getUseDeveloperSupport() {
//		  return BuildConfig.DEBUG;
//		}
//
//		@Override
//		protected List<ReactPackage> getPackages() {
//		  @SuppressWarnings("UnnecessaryLocalVariable")
//		  List<ReactPackage> packages = new PackageList(this).getPackages();
//		  // Packages that cannot be autolinked yet can be added manually here, for example:
//		  // packages.add(new MyReactNativePackage());
//		  packages.add(new com.reactnativewithoutexpo.MyAppPackage());
//		  return packages;
//		}
//
//		@Override
//		protected String getJSMainModuleName() {
//		  return "index";
//		}
//	  });
//
//	// ===========================================================
//	// Public static fields
//	// ===========================================================
//
//	public static final String APP_NAME = "multibiometric";
//
//	// ===========================================================
//	// React Native methods
//	// ===========================================================
//
//	  @Override
//	  public ReactNativeHost getReactNativeHost() {
//		return mReactNativeHost;
//	  }
//
//	  @Override
//	  public void onConfigurationChanged(@NonNull Configuration newConfig) {
//	 	super.onConfigurationChanged(newConfig);
//		ApplicationLifecycleDispatcher.onConfigurationChanged(this, newConfig);
//	  }
//
//	/**
//	 * Loads Flipper in React Native templates. Call this in the onCreate method with something like
//	 * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
//	 *
//	 * @param context
//	 * @param reactInstanceManager
//	 */
//	  private static void initializeFlipper(
//		Context context, ReactInstanceManager reactInstanceManager) {
//		if (BuildConfig.DEBUG) {
//		try {
//			/*
//			We use reflection here to pick up the class that initializes Flipper,
//			since Flipper library is not available in release mode
//			*/
//			Class<?> aClass = Class.forName("com.reactnativewithoutexpo.ReactNativeFlipper");
//			aClass
//				.getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
//				.invoke(null, context, reactInstanceManager);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		}
//	  }
//	// ===========================================================
//	// Public methods
//	// ===========================================================
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		Log.d("----------------------------------->", TAG + " onCreate");
//		SoLoader.init(this, /* native exopackage */ false);
//
//		try {
//			NLicenseManager.setTrialMode(LicensingPreferencesFragment.isUseTrial(this));
//			System.setProperty("jna.nounpack", "true");
//			System.setProperty("java.io.tmpdir", getCacheDir().getAbsolutePath());
//		} catch (Exception e) {
//			Log.e(TAG, "Exception", e);
//		}
//
//		initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
//		ApplicationLifecycleDispatcher.onApplicationCreate(this);
//	}
//}
