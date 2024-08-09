package com.parthiv.rapidrescue.AppCheck

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory

class RapidRescue : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        initializeFirebase()

        // Initialize Firebase App Check with the SafetyNet provider
        initializeAppCheck()
    }

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun initializeAppCheck() {
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}

