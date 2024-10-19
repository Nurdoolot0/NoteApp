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
}
