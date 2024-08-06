package com.parthiv.rapidrescue

import android.app.Application
import com.google.firebase.database.FirebaseDatabase


class FirebaseOffline : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase Database with offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize osmdroid configuration
        val osmConfig = org.osmdroid.config.Configuration.getInstance()
        osmConfig.load(this, getSharedPreferences("osmdroid", 0))
    }
}

