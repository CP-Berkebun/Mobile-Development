package com.capstone.berkebunplus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.berkebunplus.data.remote.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {
    val weatherData = repository.weatherData

    fun fetchWeather() {
        viewModelScope.launch {
            repository.getWeather()
        }
    }
}