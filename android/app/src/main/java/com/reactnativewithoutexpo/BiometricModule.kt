package com.reactnativewithoutexpo

import android.app.Activity
import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
//import com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity // Ensure you have the correct import for MultiModalActivity

class BiometricModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "BiometricModule"
    }

//    @ReactMethod
//    fun navigateToMultiModalActivity() {
//        val activity: Activity? = currentActivity
//        if (activity != null) {
//            val intent = Intent(activity, MultiModalActivity::class.java)
//            activity.startActivity(intent)
//        }
//    }
}
