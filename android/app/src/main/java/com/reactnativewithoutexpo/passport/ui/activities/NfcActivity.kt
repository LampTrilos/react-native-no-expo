package com.reactnativewithoutexpo.passport.ui.activities

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.widget.Toast
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import com.jillaraz.passportreader.common.IntentData
import com.jillaraz.passportreader.data.Passport
import com.reactnativewithoutexpo.MainApplication

import net.sf.scuba.smartcards.CardServiceException


import org.jmrtd.lds.icao.MRZInfo

import com.reactnativewithoutexpo.R
import com.reactnativewithoutexpo.passport.ui.fragments.NfcFragment
import com.reactnativewithoutexpo.passport.ui.fragments.PassportDetailsFragment
import com.reactnativewithoutexpo.passport.ui.fragments.PassportPhotoFragment
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable

import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactInstanceManager

class NfcActivity : androidx.fragment.app.FragmentActivity(), NfcFragment.NfcFragmentListener, PassportDetailsFragment.PassportDetailsFragmentListener, PassportPhotoFragment.PassportPhotoFragmentListener {

    private var mrzInfo: MRZInfo? = null

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        val intent = intent
        if (intent.hasExtra(IntentData.KEY_MRZ_INFO)) {
            mrzInfo = intent.getSerializableExtra(IntentData.KEY_MRZ_INFO) as MRZInfo
        } else {
            onBackPressed()
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, getString(R.string.warning_no_nfc), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE)
        } else{
            PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        }


        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NfcFragment.newInstance(mrzInfo!!), TAG_NFC)
                    .commit()
        }
    }

    public override fun onResume() {
        super.onResume()

    }

    public override fun onPause() {
        super.onPause()

    }

    public override fun onNewIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            // drop NFC events
            handleIntent(intent)
        }else{
            super.onNewIntent(intent)
        }
    }

    protected fun handleIntent(intent: Intent) {
        val fragmentByTag = supportFragmentManager.findFragmentByTag(TAG_NFC)
        if (fragmentByTag is NfcFragment) {
            fragmentByTag.handleNfcTag(intent)
        }
    }


    /////////////////////////////////////////////////////
    //
    //  NFC Fragment events
    //
    /////////////////////////////////////////////////////

    override fun onEnableNfc() {


        if (nfcAdapter != null) {
            if (!nfcAdapter!!.isEnabled)
                showWirelessSettings()

            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onDisableNfc() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }
//When the passport is successfully read
//    override fun onPassportRead(passport: Passport?) {
//        showFragmentDetails(passport!!)
//    }
    override fun onPassportRead(passport: Passport?) {
    val intent = Intent()
    intent.putExtra(IntentData.KEY_PASSPORT, passport)
    setResult(Activity.RESULT_OK, intent)
    println("NFC ACTIVITY KOTLIN: $passport")
    //The passport cannot be sent back to MainActivity cause of the size, so we emit it directly to Javascript
    if (passport != null) {
        emitNFCScanResult(passport)
    }
    //With finish it will return to Main Activity, where the listener awaits
    finish()
    }

    //The passport cannot be sent back to MainActivity cause of the size, so we emit it directly to Javascript
    private fun emitNFCScanResult(passportInfo: Passport) {
        // Convert the passportInfo to JSON string
        val gson = Gson()
        //val passportJson = gson.toJson(passportInfo)
        //The class responsible for showing the data in the original app is PassportDetailsFragment
        val passportJson = Gson()
        passportJson.put("faceImage", bitmapToBase64(passportInfo?.face))
        passportJson.put("nationality", passportInfo?.personDetails?.nationality)
        passportJson.put("dateOfBirth", passportInfo?.personDetails?.dateOfBirth)
        passportJson.put("gender", passportInfo?.personDetails?.gender.name)
        //This code is from class PassportDetailsFragment
        val name = personDetails.primaryIdentifier!!.replace("<", "")
        passportJson.put("firstName", passportInfo?.personDetails?.name) //Doesn't exist?
        val surname = personDetails.secondaryIdentifier!!.replace("<", "")
        passportJson.put("familyName", passportInfo?.personDetails?.surname) //Doesn't exist?

        jsonObject.put("issueCountry", passportInfo?.personDetails?.issuingState)
        jsonObject.put("documentNumber", passportInfo?.personDetails?.documentNumber)
        jsonObject.put("dateOfExpiry", passportInfo?.personDetails?.dateOfExpiry)

        //jsonObject.put("type", sodFile?.type)

        //Perhaps implement later
//        passportJson.put("type", sodFile?.type)
//        passportJson.put("issueCountry", sodFile?.issueCountry)
//        passportJson.put("documentNumber", sodFile?.documentNumber)
//        passportJson.put("dateOfExpiry", sodFile?.dateOfExpiry)
//        passportJson.put("chipChecked", sodFile?.chipChecked)
//        passportJson.put("mrzChecked", sodFile?.mrzChecked)

        // Create a params map
        val params = Arguments.createMap()
        params.putString("nfcData", passportJson.toString())


        val reactNativeHost = (application as MainApplication).reactNativeHost
        // Get the ReactApplicationContext from your application
        val reactApplicationContext = (application as MainApplication).reactNativeHost.reactInstanceManager.currentReactContext

        // Emit the event back to JavaScript
        reactApplicationContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            ?.emit("onNFCDataReceived", params)
    }

    //Convert Image to Base64
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    override fun onCardException(cardException: Exception?) {
        //Toast.makeText(this, cardException.toString(), Toast.LENGTH_SHORT).show();
        //onBackPressed();
    }

    private fun showWirelessSettings() {
        Toast.makeText(this, getString(R.string.warning_enable_nfc), Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }


    private fun showFragmentDetails(passport: Passport) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, PassportDetailsFragment.newInstance(passport))
                .addToBackStack(TAG_PASSPORT_DETAILS)
                .commit()
    }

    private fun showFragmentPhoto(bitmap: Bitmap) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, PassportPhotoFragment.newInstance(bitmap))
                .addToBackStack(TAG_PASSPORT_PICTURE)
                .commit()
    }


    override fun onImageSelected(bitmap: Bitmap?) {
        showFragmentPhoto(bitmap!!)
    }

    companion object {

        private val TAG = NfcActivity::class.java.simpleName


        private val TAG_NFC = "TAG_NFC"
        private val TAG_PASSPORT_DETAILS = "TAG_PASSPORT_DETAILS"
        private val TAG_PASSPORT_PICTURE = "TAG_PASSPORT_PICTURE"
    }
}
