package com.example.univote.utils

import android.content.Context
import android.content.Intent
import com.example.univote.UniVoteApp
import com.example.univote.activities.LoginActivity

object TokenManager {
    private const val KEY_TOKEN = "jwt_token"

    /**
     * Function to save token in secure storage
     */
    fun saveToken(token: String) {
        UniVoteApp.securePrefs.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Function to retrieve token from secure storage
     */
    fun getToken(): String? {
        return UniVoteApp.securePrefs.getString(KEY_TOKEN, null)
    }

    /**
     * Function to clear the token from secure storage
     */
    fun clearToken() {
        UniVoteApp.securePrefs.edit().remove(KEY_TOKEN).apply()
    }
    // New logout function to clear token and navigate to LoginActivity
    fun logoutAndNavigateToLogin(context: Context) {
        clearToken()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}