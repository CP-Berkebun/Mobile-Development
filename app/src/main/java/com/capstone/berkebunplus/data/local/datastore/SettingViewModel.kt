package com.capstone.berkebunplus.data.local.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.berkebunplus.data.BerkebunRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: BerkebunRepository) : ViewModel() {

    fun setOnboarded(onboarded: Boolean) {
        viewModelScope.launch {
            repository.setOnboarded(onboarded)
        }
    }

    fun getOnboardingStatus() = repository.getOnboardingStatus()
}