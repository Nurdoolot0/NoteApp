package com.example.noteapp.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.convertTo
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.noteapp.R
import com.example.noteapp.utils.PreferenceHelper

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceHelper()
        sharedPreferences.unit(this)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (sharedPreferences.isFirstLaunch) {
            navController.navigate(R.id.onBoardFragment)
            sharedPreferences.isFirstLaunch = false
        } else {
            navController.navigate(R.id.noteFragment)
        }
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentFragment = navController.currentDestination?.id
            if (currentFragment == R.id.noteFragment) {
                finish()
            } else {
                navController.navigateUp()
            }
        }
    })
}
}