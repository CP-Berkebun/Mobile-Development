package com.capstone.berkebunplus.di

import com.capstone.berkebunplus.data.BerkebunRepository
import com.capstone.berkebunplus.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): BerkebunRepository {
        val apiService = ApiConfig.getApiService()
        return BerkebunRepository.getInstance(apiService)
    }
}