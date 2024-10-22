package com.reactnativewithoutexpo

// Add this import for gesture handler
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.jillaraz.passportreader.common.IntentData
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.lang.NCore
import com.neurotec.samples.app.BaseActivity
import com.neurotec.samples.licensing.LicensingManager
import com.neurotec.samples.util.ToastManager
import com.reactnativewithoutexpo.multibiometric.multimodal.FaceActivity
import com.reactnativewithoutexpo.multibiometric.multimodal.FingerActivity
import com.reactnativewithoutexpo.multibiometric.multimodal.MultiModalActivity
import org.jmrtd.lds.icao.MRZInfo


class MainActivity  : ReactActivity() {
//class MainActivity : BaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

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
        //To set up for License Manager during onResume
        //NCore.setContext(this)
        //ActivityCompat.requestPermissions(this, getRequiredPermissions(),1);
        //getLicensesAndPermissions()
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
//        ActivityCompat.requestPermissions(this, getRequiredPermissions(),1);
//        getLicensesAndPermissions()
    }


    private fun getRequiredPermissions(): Array<String> {
        val permissions: MutableList<String> = ArrayList()
        permissions.add(Manifest.permission.INTERNET)
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE)
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.RECORD_AUDIO)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return permissions.toTypedArray();
    }

    private fun getLicensesAndPermissions(vararg params: Any): Boolean {
        //// We don't have permission so prompt the user to Get permission for camera
//		List<String> permissions = new ArrayList<>();
//		permissions.add(Manifest.permission.CAMERA);
//		ActivityCompat.requestPermissions(this, permissions.toArray(new String[1]), REQUEST_ID_MULTIPLE_PERMISSIONS);
//		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//		}
//
//        String[] neededPermissions = getNotGrantedPermissions();
//
//        requestPermissions(neededPermissions);

		//showProgress(R.string.msg_obtaining_licenses);

//		try {
//			LicensingManager.getInstance().obtain(this, getAdditionalComponentsInternal());
//			if (LicensingManager.getInstance().obtain(this, getMandatoryComponentsInternal())) {
//				showToast(R.string.msg_licenses_obtained);
//			} else {
//				showToast(R.string.msg_licenses_partially_obtained);
//			}
//		} catch (Exception e) {
//			showError(e.getMessage(), false);
//		}
//		BaseActivity.showProgress(R.string.msg_initializing_client);
//
//        try {
//            NBiometricClient client = Model .getInstance().getClient();
//            //client = Model.getInstance().client
//        } catch (e: Exception) {
//            Log.e("Face", e.message, e)
//            return false
//        }
        return true
    }

    protected fun showToast(messageId: Int) {
        showToast(getString(messageId))
    }

    protected fun showToast(message: String?) {
        runOnUiThread {
            ToastManager.show(
                this@MainActivity,
                message
            )
        }
    }


    //	private TextView mIrisCounter;
    //	private TextView mVoiceCounter;
    // ===========================================================
    // Private methods
    // ===========================================================
    fun getMandatoryComponentsInternal(): List<String> {
        val components: MutableList<String> = java.util.ArrayList()
        for (component in FaceActivity.mandatoryComponents()) {
            if (!components.contains(component)) {
                components.add(component)
            }
        }
        for (component in FingerActivity.mandatoryComponents()) {
            if (!components.contains(component)) {
                components.add(component)
            }
        }
        /*for (String component : IrisActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : VoiceActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}*/
        return components
    }

    fun getAdditionalComponentsInternal(): List<String> {
        val components: MutableList<String> = java.util.ArrayList()
        for (component in FaceActivity.additionalComponents()) {
            if (!components.contains(component)) {
                components.add(component)
            }
        }
        for (component in FingerActivity.additionalComponents()) {
            if (!components.contains(component)) {
                components.add(component)
            }
        }
        /*		for (String component : IrisActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : VoiceActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}*/
        return components
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
            //If we sent mrz data, send the corresponding event
                // Pass it back to JavaScript
                val params = Arguments.createMap()
                params.putString(
                    "mrzData",
                    mrzInfo.toString()
                )
                val reactContext = reactApplicationContext()
                if (reactContext.hasActiveReactInstance()) {
                    reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("onMRZDataReceived", params)
                }
        }
    }
}
