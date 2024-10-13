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

    //Method that is called by PassportScan to open the native CameraActivity
    @ReactMethod
    fun navigateToMRZActivity() {
        Log.d("PassportModule", "Called PassportModule Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad fASDFSADFSADF SDAFSAFasdf")
        val activity = currentActivity
        if (activity != null) {
            Log.d("PassportModule", "Called PassportModule Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad.. Activity is  not null")
            val intent =
                Intent(activity, com.reactnativewithoutexpo.passport.ui.activities.CameraActivity::class.java)
            //This ensures that the activity will return something
            //When the activity finishes, it will trigger MainACtivity.onActivityResult, which will emit an event that will finally update Javascript about the returned data
            activity.startActivityForResult(intent, 200)
        }
    }

}