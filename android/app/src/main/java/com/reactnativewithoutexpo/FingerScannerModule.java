package com.reactnativewithoutexpo;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import androidx.annotation.NonNull;

public class FingerScannerModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;

    FingerScannerModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "FingerPrintScannerModule";
    }

}