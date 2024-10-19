1) To be able to use the finger scanner on T80:
   Open the T80 Hub and enable T80 HUB and T80 FP
2) For license Activation, i tested with Mutlimodal app and placed the .sn serial files in sdcard0.Neurotechnology
   From licensing menu disable trial mode, then chose activate and pick the folder with the serial number files
   It seems that Multimodal app has created two .lic files, but for now i can't find them

!!!!!!!
Removed lib++ from android/libs as it caused a mismatch problem

Error when photo
FATAL EXCEPTION: main
Process: com.reactnativewithoutexpo, PID: 5791
android.content.res.Resources$NotFoundException: String resource ID #0x7f020020
at android.content.res.Resources.getText(Resources.java:444)
at android.content.res.Resources.getString(Resources.java:537)
at android.content.Context.getString(Context.java:705)
at com.neurotec.biometrics.view.NFaceView.loadStrings(NFaceView.java:349)
at com.neurotec.biometrics.view.NFaceView.initComponents(NFaceView.java:429)
at com.neurotec.biometrics.view.NFaceView.<init>(NFaceView.java:304)
at com.reactnativewithoutexpo.multibiometric.multimodal.FaceActivity.startCapturing(FaceActivity.java:92)
at com.reactnativewithoutexpo.multibiometric.multimodal.FaceActivity.onLicensesObtained(FaceActivity.java:238)
at com.reactnativewithoutexpo.multibiometric.multimodal.BiometricActivity$InitializationTask.onPostExecute(BiometricActivity.java:549)
at com.reactnativewithoutexpo.multibiometric.multimodal.BiometricActivity$InitializationTask.onPostExecute(BiometricActivity.java:518)
at android.os.AsyncTask.finish(AsyncTask.java:771)
at android.os.AsyncTask.access$900(AsyncTask.java:199)
at android.os.AsyncTask$InternalHandler.handleMessage(AsyncTask.java:788)
at android.os.Handler.dispatchMessage(Handler.java:106)
at android.os.Looper.loopOnce(Looper.java:201)
at android.os.Looper.loop(Looper.java:288)
at android.app.ActivityThread.main(ActivityThread.java:7881)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:568)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1045)


and then 


Unable to instantiate fragment com.swmansion.rnscreens.ScreenFragment: calling Fragment constructor caused an exception




This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up _correctly_, you should see your new app running in your _Android Emulator_ or _iOS Simulator_ shortly provided you have set up your emulator/simulator correctly.

This is one way to run your app — you can also run it directly from within Android Studio and Xcode respectively.

## Step 3: Modifying your App

Now that you have successfully run the app, let's modify it.

1. Open `App.tsx` in your text editor of choice and edit some lines.
2. For **Android**: Press the <kbd>R</kbd> key twice or select **"Reload"** from the **Developer Menu** (<kbd>Ctrl</kbd> + <kbd>M</kbd> (on Window and Linux) or <kbd>Cmd ⌘</kbd> + <kbd>M</kbd> (on macOS)) to see your changes!

   For **iOS**: Hit <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> in your iOS Simulator to reload the app and see your changes!

## Congratulations! :tada:

You've successfully run and modified your React Native App. :partying_face:

### Now what?

- If you want to add this new React Native code to an existing application, check out the [Integration guide](https://reactnative.dev/docs/integration-with-existing-apps).
- If you're curious to learn more about React Native, check out the [Introduction to React Native](https://reactnative.dev/docs/getting-started).

# Troubleshooting

If you can't get this to work, see the [Troubleshooting](https://reactnative.dev/docs/troubleshooting) page.

# Learn More

To learn more about React Native, take a look at the following resources:

- [React Native Website](https://reactnative.dev) - learn more about React Native.
- [Getting Started](https://reactnative.dev/docs/environment-setup) - an **overview** of React Native and how setup your environment.
- [Learn the Basics](https://reactnative.dev/docs/getting-started) - a **guided tour** of the React Native **basics**.
- [Blog](https://reactnative.dev/blog) - read the latest official React Native **Blog** posts.
- [`@facebook/react-native`](https://github.com/facebook/react-native) - the Open Source; GitHub **repository** for React Native.
