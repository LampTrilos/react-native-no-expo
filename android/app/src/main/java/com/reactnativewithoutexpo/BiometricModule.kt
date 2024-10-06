package com.reactnativewithoutexpo

import android.content.Intent
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
        val activity = currentActivity
        if (activity != null) {
            val intent =
                Intent(activity, com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity::class.java)
            activity.startActivity(intent)
        }
    }
}