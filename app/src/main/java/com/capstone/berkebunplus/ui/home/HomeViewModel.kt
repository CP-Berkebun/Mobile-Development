package com.capstone.berkebunplus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.berkebunplus.data.BerkebunRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: BerkebunRepository) : ViewModel() {
    val weatherData = repository.weatherData

    fun fetchWeather() {
        viewModelScope.launch {
            repository.getWeather()
        }
    }
}