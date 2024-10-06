package com.reactnativewithoutexpo.multibiometric.preferences

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.*
import com.neurotec.samples.view.BasePreferenceFragment
import com.neurotec.samples.view.InfoDialogFragment

class ConnectionPreferences : PreferenceActivity() {
    enum class ConnectionType {
        SQLITE,
        CLUSTER,
        MMABIS
    }

    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActionBar().setDisplayHomeAsUpEnabled(false)
        getFragmentManager().beginTransaction().replace(R.id.content, ConnectionPreferencesFragment()).commit()
    }

    class ConnectionPreferencesFragment : BasePreferenceFragment() {
        private var showInfo = true

        // ===========================================================
        // Public methods
        // ===========================================================
        private fun showInfoMessage() {
            if (showInfo) {
                InfoDialogFragment.newInstance("Changing connection requires application to be restarted")
                    .show(getFragmentManager(), "info")
                showInfo = false
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.connection_preferences)
        }

        private val isAtleastOneConnectionSelected: Boolean
            get() = (findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE) as SwitchPreference).isChecked() ||
                    (findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE) as SwitchPreference).isChecked() ||
                    (findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE) as SwitchPreference).isChecked()

        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference): Boolean {
            if (preference.getKey() == SQLITE_DATABASE_CONNECTION_PREFERENCE) {
                if (isAtleastOneConnectionSelected) {
                    (findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE) as SwitchPreference).setChecked(false)
                } else {
                    (findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE) as SwitchPreference).setChecked(true)
                }
                //((SwitchPreference)findPreference(REMOTE_MMABIS_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage()
            } else if (preference.getKey() == REMOTE_CLUSTER_SERVER_PREFERENCE) {
                if (isAtleastOneConnectionSelected) {
                    (findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE) as SwitchPreference).setChecked(false)
                } else {
                    (findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE) as SwitchPreference).setChecked(true)
                }
                //((SwitchPreference)findPreference(REMOTE_MMABIS_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage()
                /*} else if (preference.getKey().equals(REMOTE_MMABIS_SERVER_PREFERENCE)) {
                ((SwitchPreference)findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE)).setChecked(false);
                ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage();*/
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference)
        }
    }

    companion object {
        // ===========================================================
        // Public static fields
        // ===========================================================
        const val SQLITE_DATABASE_CONNECTION_PREFERENCE: String = "sqlite_database_connection"
        const val REMOTE_CLUSTER_SERVER_PREFERENCE: String = "remote_cluster_server"
        const val REMOTE_MMABIS_SERVER_PREFERENCE: String = "remote_mmabis_server"

        const val CLUSTER_SERVER_ADDRESS_PREFERENCE: String = "cluster_server_address"
        const val CLUSTER_CLIENT_PORT_PREFERENCE: String = "cluster_client_port"
        const val CLUSTER_ADMIN_PORT_PREFERENCE: String = "cluster_admin_port"

        const val MMABIS_SERVER_PREFERENCE: String = "remote_mmabis_server"
        const val MMABIS_SERVER_ADDRESS_PREFERENCE: String = "mmabis_server_address"
        const val MMABIS_USERNAME_PREFERENCE: String = "mmabis_username"
        const val MMABIS_PASSWORD_PREFERENCE: String = "mmabis_password"

        fun getConnectionType(context: Context?): ConnectionType {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            return if (preferences.getBoolean(
                    SQLITE_DATABASE_CONNECTION_PREFERENCE,
                    true
                )
            ) {
                ConnectionType.SQLITE
            } else if (preferences.getBoolean(
                    REMOTE_CLUSTER_SERVER_PREFERENCE,
                    false
                )
            ) {
                ConnectionType.CLUSTER
            } else if (preferences.getBoolean(REMOTE_MMABIS_SERVER_PREFERENCE, false)) {
                ConnectionType.MMABIS
            } else {
                throw AssertionError("Was unable to detect connection type")
            }
        }

        fun getClusterServerAddress(context: Context?): String? {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(CLUSTER_SERVER_ADDRESS_PREFERENCE, "localhost")
        }

        fun getClusterClientPort(context: Context?): Int {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(CLUSTER_CLIENT_PORT_PREFERENCE, "25452").toInt()
        }

        fun getClusterAdminPort(context: Context?): Int {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(CLUSTER_ADMIN_PORT_PREFERENCE, "24932").toInt()
        }

        fun getMMABISServerAddress(context: Context?): String? {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(MMABIS_SERVER_ADDRESS_PREFERENCE, "megamatcher.online")
        }

        fun getMMABISUsername(context: Context?): String? {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(MMABIS_USERNAME_PREFERENCE, "user")
        }

        fun getMMABISPassword(context: Context?): String? {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(MMABIS_PASSWORD_PREFERENCE, "pass")
        }
    }
}
