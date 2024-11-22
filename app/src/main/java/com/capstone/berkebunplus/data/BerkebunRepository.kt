package com.capstone.berkebunplus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.berkebunplus.data.remote.response.WeatherResponse
import com.capstone.berkebunplus.data.remote.retrofit.ApiService

class BerkebunRepository(private val apiServiceWeather: ApiService) {
    private val _weatherData = MutableLiveData<Result<WeatherResponse>>()
    val weatherData: LiveData<Result<WeatherResponse>> = _weatherData

    suspend fun getWeather() {
        _weatherData.postValue(Result.Loading)
        try {
            val response = apiServiceWeather.getWeather()
            _weatherData.postValue(Result.Success(response))
        } catch (e: Exception) {
            _weatherData.postValue(Result.Error("Error: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: BerkebunRepository? = null
        fun getInstance(
            apiService: ApiService
        ): BerkebunRepository =
            instance ?: synchronized(this) {
                instance ?: BerkebunRepository(apiService)
            }.also { instance = it }
    }
}