package com.capstone.berkebunplus.data.local.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {

    fun checkOnboardingStatus(onBoardingCallback : (Boolean) -> Unit) {
        viewModelScope.launch {
            val isOnboarded = settingPreferences.isOnboarded.first()
            onBoardingCallback(isOnboarded)
        }
    }

    fun setOnboarded(onboarded: Boolean) {
        viewModelScope.launch {
            settingPreferences.setOnboarded(onboarded)
        }
    }
}