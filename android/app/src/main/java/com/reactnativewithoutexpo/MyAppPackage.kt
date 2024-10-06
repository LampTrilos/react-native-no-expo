package com.reactnativewithoutexpo

import android.util.Log
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

// replace your-app-name with your app’s name

class MyAppPackage : ReactPackage {
    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        Log.d("-----------------------------------@@> create BiometricModule", "set reactContext")
        val modules: MutableList<NativeModule> = ArrayList()

        modules.add(BiometricModule(reactContext))

        return modules
    }
}