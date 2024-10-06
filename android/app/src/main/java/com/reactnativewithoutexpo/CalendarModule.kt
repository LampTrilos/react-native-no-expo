package com.reactnativewithoutexpo; // replace your-apps-package-name with your app’s package name

import android.util.Log
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class CalendarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // add to CalendarModule.kt
    override fun getName() = "CalendarModule"

    @ReactMethod
    fun createCalendarEvent(name: String, location: String) {
        Log.d("CalendarModule", "Create event called with name: $name and location: $location")
    }

    //Test method to start multi modal activity
//    @ReactMethod
//    fun startMultiModalActivity() {
//        val intent = Intent(reactApplicationContext, MultiModalActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Ensure the activity is launched from a non-activity context
//        reactApplicationContext.startActivity(intent)
//    }

}