package com.capstone.berkebunplus.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.berkebunplus.ui.OnBoardingPreferences
import com.capstone.berkebunplus.ui.dataStore

@Suppress("UNCHECKED_CAST")
class SplashScreenViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(OnBoardingPreferences(context.dataStore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}