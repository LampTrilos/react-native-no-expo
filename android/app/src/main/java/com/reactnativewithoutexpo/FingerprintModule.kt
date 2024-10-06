package com.reactnativewithoutexpo; // replace your-apps-package-name with your app’s package name

import android.util.Log
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class FingerprintModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CalendarModule.kt
    override fun getName() = "FingerprintModule"

    @ReactMethod
    fun createCalendarEvent(name: String, location: String) {
        Log.d("FingerprintModule", "FingerprintModule Create Fingerprint called with name: $name and location: $location")
    }

    @ReactMethod
    fun startBiometricScan() {
        // Use SDK methods to start biometric scan
    }
}