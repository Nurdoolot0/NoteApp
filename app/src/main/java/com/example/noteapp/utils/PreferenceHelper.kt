package com.example.noteapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
    }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean("isFirstLaunch", true)
        set(value) = sharedPreferences.edit().putBoolean("isFirstLaunch", value).apply()

    var text: String?
        get() = sharedPreferences.getString("text", "")
        set(value) = sharedPreferences.edit().putString("text", value)!!.apply()

    var isLinearLayout: Boolean
        get() = sharedPreferences.getBoolean("isLinearLayout", true)
        set(value) = sharedPreferences.edit().putBoolean("isLinearLayout", value).apply()

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun isOnBoardShown(): Boolean {
        return sharedPreferences.getBoolean("has_seen_onboard", false)
    }

    fun setOnBoardShown(shown: Boolean) {
        sharedPreferences.edit().putBoolean("has_seen_onboard", shown).apply()
    }

    fun isSignedUp(): Boolean {
        return sharedPreferences.getBoolean("is_signed_up", false)
    }

    fun setSignedUp(signedUp: Boolean) {
        sharedPreferences.edit().putBoolean("is_signed_up", signedUp).apply()
    }
}
