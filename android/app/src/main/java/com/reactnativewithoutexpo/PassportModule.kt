package com.reactnativewithoutexpo

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule


class PassportModule internal constructor(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "PassportModule"
    }

    @ReactMethod
    fun navigateToMRZActivity() {
        Log.d("PassportModule", "Called PassportModule Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad fASDFSADFSADF SDAFSAFasdf")
        val activity = currentActivity
        if (activity != null) {
            Log.d("PassportModule", "Called PassportModule Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad.. Activity is  not null")
            val intent =
                Intent(activity, com.reactnativewithoutexpo.passport.ui.activities.CameraActivity::class.java)
            //activity.startActivity(intent)
            // Make sure to use a request code when a result is returned
            activity.startActivityForResult(intent, 200)
        }
    }

    // Method to send MRZ data to JavaScript
    fun sendMRZData(mrzData: String?) {
        println(mrzData)
        // Check if the ReactContext is not null
        if (reactApplicationContext.hasActiveCatalystInstance()) {
            // Create a map to hold your parameters
            val params = Arguments.createMap()
            params.putString("mrzData", mrzData)

            // Send the event
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onMRZDataReceived", params)
        }
    }
}