package com.capstone.berkebunplus.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.data.remote.response.WeatherResponse
import com.capstone.berkebunplus.data.remote.retrofit.ApiService

class WeatherRepository(private val apiService: ApiService) {
    private val _weatherData = MutableLiveData<Result<WeatherResponse>>()
    val weatherData: LiveData<Result<WeatherResponse>> = _weatherData

    suspend fun getWeather() {
        _weatherData.postValue(Result.Loading)
        try {
            val response = apiService.getWeather()
            _weatherData.postValue(Result.Success(response))
        } catch (e: Exception) {
            _weatherData.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null
        fun getInstance(
            apiService: ApiService
        ): WeatherRepository =
            instance ?: synchronized(this) {
                instance ?: WeatherRepository(apiService)
            }.also { instance = it }
    }
}