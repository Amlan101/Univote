package com.example.univote.utils

import com.example.univote.UniVoteApp

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
}