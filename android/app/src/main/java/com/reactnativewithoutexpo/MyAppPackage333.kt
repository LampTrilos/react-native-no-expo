package com.reactnativewithoutexpo // replace your-app-name with your app’s name

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class MyAppPackage333 : ReactPackage {

    override fun createViewManagers(
        reactContext: ReactApplicationContext
    ): MutableList<ViewManager<View, ReactShadowNode<*>>> = mutableListOf()

    override fun createNativeModules(
        reactContext: ReactApplicationContext
    ): MutableList<NativeModule> =   mutableListOf<NativeModule>(
        //We added the Calendar Module in out package so we can call the modules methods from the javascript files
        CalendarModule(reactContext),
        //FingerPrintModule(reactContext),
        //BiometricModule(reactContext) // Add more modules here
    )
}