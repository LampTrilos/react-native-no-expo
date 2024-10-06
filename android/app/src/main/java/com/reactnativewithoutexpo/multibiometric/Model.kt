package com.reactnativewithoutexpo.multibiometric

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.neurotec.biometrics.NBiometricOperation
import com.neurotec.biometrics.NSubject
import com.neurotec.biometrics.client.NBiometricClient
import com.neurotec.biometrics.client.NMMAbisConnection
import com.neurotec.lang.NCore
import com.neurotec.samples.util.IOUtils
import com.reactnativewithoutexpo.multibiometric.preferences.ConnectionPreferences
import com.reactnativewithoutexpo.multibiometric.preferences.FacePreferences
import com.reactnativewithoutexpo.multibiometric.preferences.FingerPreferences
import java.util.*

class Model private constructor() : ReactContextBaseJavaModule(reactContext) {
    // ===========================================================
    // Private fields
    // ===========================================================
    private val mClient: NBiometricClient
    private val mSubject: NSubject

    private var mSubjects: Array<NSubject>

    init {
        mClient = NBiometricClient()
        updateClientConnection(mClient)
        mClient.setUseDeviceManager(true)
        mClient.setMatchingWithDetails(true)
        mClient.setProperty("Faces.IcaoUnnaturalSkinToneThreshold", 10)
        mClient.setProperty("Faces.IcaoSkinReflectionThreshold", 10)
        mClient.initialize()
        mSubjects = arrayOf<NSubject>()
        mSubject = NSubject()
    }

    val client: NBiometricClient
        // ===========================================================
        get() = mClient

    val subject: NSubject
        get() = mSubject

    var subjects: Array<NSubject>
        /**
         * Subjects contain copy of subject list from biometric client
         * so that list could be accessible while continuous tasks are being
         * performed on biometric client like capturing from camera
         */
        get() = mSubjects
        /**
         * Subjects contain copy of subject list from biometric client
         * so that list could be accessible while continuous tasks are being
         * performed on biometric client like capturing from camera
         */
        set(subjects) {
            this.mSubjects = subjects
        }

    fun update() {
        FingerPreferences.updateClient(mClient, NCore.getContext())
        FacePreferences.updateClient(mClient, NCore.getContext())
    }

    val name: String
        get() = "Model"

    companion object {
        private var reactContext: ReactApplicationContext? = null


        // ===========================================================
        // Private static fields
        // ===========================================================
        private var sInstance: Model? = null

        val instance: Model
            // ===========================================================
            get() {
                synchronized(Model::class.java) {
                    if (sInstance == null) {
                        sInstance =
                            Model()
                    }
                    return sInstance!!
                }
            }

        // ===========================================================
        // Private constructor
        // ===========================================================
        private fun updateClientConnection(client: NBiometricClient) {
            when (ConnectionPreferences.getConnectionType(NCore.getContext())) {
                ConnectionPreferences.ConnectionType.SQLITE -> {
                    client.setDatabaseConnectionToSQLite(
                        IOUtils.combinePath(
                            NCore.getContext().getFilesDir().getAbsolutePath(), "BiometricsV50.db"
                        )
                    )
                    client.getRemoteConnections().clear()
                }

                ConnectionPreferences.ConnectionType.CLUSTER -> {
                    client.setLocalOperations(
                        EnumSet.of<NBiometricOperation>(
                            NBiometricOperation.DETECT,
                            NBiometricOperation.DETECT_SEGMENTS,
                            NBiometricOperation.SEGMENT,
                            NBiometricOperation.ASSESS_QUALITY,
                            NBiometricOperation.CREATE_TEMPLATE
                        )
                    )
                    val port: Int = ConnectionPreferences.getClusterClientPort(NCore.getContext())

                    val adminPort: Int = ConnectionPreferences.getClusterAdminPort(NCore.getContext())
                    val host: String = ConnectionPreferences.getClusterServerAddress(NCore.getContext()).toString()
                    client.getRemoteConnections().add(NClusterBiometricConnection(host, port, adminPort))
                }

                MMABIS -> {
                    client.setLocalOperations(EnumSet.noneOf<NBiometricOperation>(NBiometricOperation::class.java))
                    val address: String = ConnectionPreferences.getMMABISServerAddress(NCore.getContext()).toString()
                    val username: String = ConnectionPreferences.getMMABISUsername(NCore.getContext()).toString()
                    val password: String = ConnectionPreferences.getMMABISPassword(NCore.getContext()).toString()
                    client.getRemoteConnections().add(NMMAbisConnection(address, username, password))
                }
            }
        }

        fun setReactContext(context: ReactApplicationContext?) {
            reactContext = context
        }

        fun getReactContext(): ReactApplicationContext? {
            return reactContext
        }
    }
}
