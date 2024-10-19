package com.reactnativewithoutexpo

import android.app.Application
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader
import com.neurotec.licensing.NLicenseManager
import com.neurotec.licensing.gui.LicensingPreferencesFragment

class MainApplication : Application(), ReactApplication {


  // ===========================================================
  // Private static fields
  // ===========================================================
   val TAG: String = com.reactnativewithoutexpo.MainApplication::class.java.simpleName


  // ===========================================================
  // Public static fields
  // ===========================================================
   val APP_NAME: String = "multibiometric"


  override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              // add(MyReactNativePackage())
                add(MyAppPackage())
            }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
      }


  override val reactHost: ReactHost
    get() = getDefaultReactHost(applicationContext, reactNativeHost)

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }
    try {
      NLicenseManager.setTrialMode(LicensingPreferencesFragment.isUseTrial(this))
      System.setProperty("jna.nounpack", "true")
      System.setProperty("java.io.tmpdir", cacheDir.absolutePath)
    } catch (e: Exception) {
      Log.e("Error", "Exception", e)
    }
  }
}
