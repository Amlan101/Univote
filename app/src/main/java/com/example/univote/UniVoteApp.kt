package com.example.univote

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class UniVoteApp: Application() {
    companion object {
        lateinit var securePrefs: SharedPreferences
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initSecurePrefs()
    }

    /**
     * Initialize EncryptedSharedPreferences for secure storage.
     */
    private fun initSecurePrefs() {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        securePrefs = getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
        securePrefs = EncryptedSharedPreferences.create(
            "secure_prefs",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}