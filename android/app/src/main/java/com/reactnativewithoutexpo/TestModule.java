package com.reactnativewithoutexpo;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.*;

public class TestModule extends ReactContextBaseJavaModule {

    public void BiometricModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "TestModule";
    }

    @ReactMethod
    public void startBiometricScan() {
        // Use SDK methods to start biometric scan
    }

    @ReactMethod
    public void logSomething(String name, String location) {
        Log.d("TestModule", "Create event called with name: " + name
                + " and location: " + location);
    }
}