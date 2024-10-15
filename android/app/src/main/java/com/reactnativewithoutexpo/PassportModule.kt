package com.reactnativewithoutexpo

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import org.jmrtd.lds.icao.MRZInfo


class PassportModule internal constructor(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "PassportModule"
    }

    //Method that is called by PassportScan to open the native CameraActivity to scan the MRZ
    @ReactMethod
    fun navigateToMRZActivity() {
        Log.d("PassportModule", "Called PassportModule Module FOR MRZ SCAN")
        val activity = currentActivity
        if (activity != null) {
            Log.d("PassportModule", "Called PassportModule Module FOR MRZ SCAN")
            val intent =
                Intent(activity, com.reactnativewithoutexpo.passport.ui.activities.CameraActivity::class.java)
            //This ensures that the activity will return something
            //When the activity finishes, it will trigger MainACtivity.onActivityResult, which will emit an event that will finally update Javascript about the returned data
            activity.startActivityForResult(intent, 200)
        }
    }


    //Method that is called by PassportScan to open the NFC Scannner to read the Passport chip
    @ReactMethod
    fun navigateToNFCActivity(data: String) {
        Log.d("PassportModule", "Called PassportModule Module for PASSPORT SCAN")
        val activity = currentActivity
        if (activity != null) {
            Log.d("PassportModule", "Called PassportModule Module for PASSPORT SCAN")
            val mrzInfo = convertStringToMRZInfo(data)
            val intent2 =
                    Intent(activity, com.reactnativewithoutexpo.passport.ui.activities.NfcActivity::class.java).apply {
                        putExtra("KEY_MRZ_INFO", mrzInfo)
                    }
            //This ensures that the activity will return something
            //When the activity finishes, it will trigger MainACtivity.onActivityResult, which will emit an event that will finally update Javascript about the returned data
            //activity.startActivityForResult(intent2, 200)
            activity.startActivity(intent2)
        }
    }

    //Convert a String to MRZinfo
    private fun convertStringToMRZInfo(mrzString: String): MRZInfo {
        return try {
            MRZInfo(mrzString)
        } catch (e: Exception) {
            // Handle any parsing errors here
            throw IllegalArgumentException("Invalid MRZ string format", e)
        }
    }

}
