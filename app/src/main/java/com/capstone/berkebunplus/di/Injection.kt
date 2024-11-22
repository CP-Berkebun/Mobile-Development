package com.capstone.berkebunplus.di

import com.capstone.berkebunplus.data.BerkebunRepository
import com.capstone.berkebunplus.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): BerkebunRepository {
        // This is API Config for Weather APIs
        val apiServiceWeather = ApiConfig.getWeatherApi()
        // Add another API Config in here (in case we will put detection APIs here)
        return BerkebunRepository.getInstance(apiServiceWeather)
    }
}