package com.capstone.berkebunplus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.berkebunplus.data.BerkebunRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: BerkebunRepository) : ViewModel() {
    fun fetchWeather(latitude: Double, longitude: Double) =
        repository.getWeather(latitude, longitude)
}