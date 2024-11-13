package com.capstone.berkebunplus.di

import com.capstone.berkebunplus.data.remote.WeatherRepository
import com.capstone.berkebunplus.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): WeatherRepository {
        val apiService = ApiConfig.getApiService()
        return WeatherRepository.getInstance(apiService)
    }
}