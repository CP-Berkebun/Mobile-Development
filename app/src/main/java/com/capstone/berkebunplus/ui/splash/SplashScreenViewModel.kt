package com.capstone.berkebunplus.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.berkebunplus.ui.OnBoardingPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val onBoardingPreferences: OnBoardingPreferences) : ViewModel() {

    fun checkOnboardingStatus(onBoardingCallback : (Boolean) -> Unit) {
        viewModelScope.launch {
            val isOnboarded = onBoardingPreferences.isOnboarded.first()
            onBoardingCallback(isOnboarded)
        }
    }
}