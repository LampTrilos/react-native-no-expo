package com.reactnativewithoutexpo.multibiometric.preferences

import android.R
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import com.neurotec.lang.NCore
import com.neurotec.samples.preferences.BasePreferenceActivity
import com.neurotec.samples.view.BasePreferenceFragment

class MultimodalPreferences : BasePreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentManager().beginTransaction().replace(R.id.content, MultimodalPreferencesFragment()).commit()
    }

    class MultimodalPreferencesFragment : BasePreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.multimodal_preferences)
        }
    }

    companion object {
        const val DUPLICATE_CHECK: String = "duplicate_check"

        val isCheckForDuplicates: Boolean
            get() {
                if (NCore.getContext() == null) throw NullPointerException("NCore.setContext() should be set")
                val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(NCore.getContext())
                return preferences.getBoolean(DUPLICATE_CHECK, true)
            }
    }
}
