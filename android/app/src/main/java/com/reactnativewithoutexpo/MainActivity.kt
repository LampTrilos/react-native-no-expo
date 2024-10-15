package com.reactnativewithoutexpo

// Add this import for gesture handler
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.jillaraz.passportreader.common.IntentData
import com.jillaraz.passportreader.data.Passport
import org.jmrtd.lds.icao.MRZInfo
import kotlin.math.log
import com.google.gson.Gson


class MainActivity : ReactActivity() {

    //  /**
//   * Returns the name of the main component registered from JavaScript. This is used to schedule
//   * rendering of the component.
//   */
    override fun getMainComponentName(): String = "ReactNativeWithoutExpo"

    //
//  /**
//   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
//   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
//   */
    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)


    // Initialize PassportModule (make sure you have the correct context)
    private lateinit var passportModule: PassportModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    // Method to get the ReactApplicationContext
    fun reactApplicationContext(): ReactApplicationContext {
        val context = reactInstanceManager.currentReactContext
        return context as? ReactApplicationContext
            ?: throw IllegalStateException("ReactApplicationContext is not available.")
    }

    // Initialize PassportModule using the ReactApplicationContext in onResume
    override fun onResume() {
        super.onResume()
    }

    //Callback listener to check for events from custom activities so our js pages get notified
    //This is needed because the CameraActivity closes and doesn't exist after the mrz is fired
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if (requestCode == 200) {
        println("MAIN ACTIVITY: ADFADF ASDFVASVF SDFVDSVF SDFV MRZ DATA IS:")
        println(data)
        if (resultCode == RESULT_OK) {
            // Get MRZ data from the Intent, if it exists
            val mrzInfo = data?.getSerializableExtra(IntentData.KEY_MRZ_INFO) as? MRZInfo
            // Get MRZ data from the Intent, if it exists
            val passportInfo = data?.getSerializableExtra(IntentData.KEY_PASSPORT) as? Passport
            //If we sent mrz data, send the corresponding event
            if (mrzInfo != null) {
                // Pass it back to JavaScript
                val params = Arguments.createMap()
                params.putString(
                    "mrzData",
                    mrzInfo.toString()
                )
                val reactContext = reactApplicationContext()
                if (reactContext.hasActiveCatalystInstance()) {
                    reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("onMRZDataReceived", params)
                }
            }
        }
    }
}
