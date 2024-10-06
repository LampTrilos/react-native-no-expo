package com.reactnativewithoutexpo

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod


class BiometricModule internal constructor(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "BiometricModule"
    }

    @ReactMethod
    fun navigateToMultiModalActivity() {
        Log.d("CalendarModule", "Called Biometric Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad fASDFSADFSADF SDAFSAFasdf")
        val activity = currentActivity
        if (activity != null) {
            Log.d("CalendarModule", "Called Biometric Module!!SADFASDF asfdASDAFAF asdFSADFSAFsad.. Activity is  not null")
            val intent =
                Intent(activity, com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity::class.java)
            activity.startActivity(intent)
        }
    }
}