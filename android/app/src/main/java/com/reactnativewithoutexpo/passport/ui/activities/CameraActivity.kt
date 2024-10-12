package com.reactnativewithoutexpo.passport.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jillaraz.passportreader.common.IntentData

import org.jmrtd.lds.icao.MRZInfo

import com.reactnativewithoutexpo.R
import com.reactnativewithoutexpo.passport.ui.fragments.CameraMLKitFragment

class CameraActivity : AppCompatActivity(), CameraMLKitFragment.CameraMLKitCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, CameraMLKitFragment())
                .commit()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onPassportRead(mrzInfo: MRZInfo) {
        val intent = Intent()
        intent.putExtra(IntentData.KEY_MRZ_INFO, mrzInfo)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onError() {
        onBackPressed()
    }

    companion object {

        private val TAG = CameraActivity::class.java.simpleName
    }
}
