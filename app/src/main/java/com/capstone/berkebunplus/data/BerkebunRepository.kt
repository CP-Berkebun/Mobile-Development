package com.capstone.berkebunplus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.capstone.berkebunplus.data.local.datastore.SettingPreferences
import com.capstone.berkebunplus.data.remote.retrofit.ApiService

class BerkebunRepository(
    private val preferences: SettingPreferences,
    private val apiServiceWeather: ApiService
) {
    fun getWeather(latitude: Double, longitude: Double) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServiceWeather.getWeather(latitude, longitude)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    suspend fun setOnboarded(onboarded: Boolean) {
        preferences.setOnboarded(onboarded)
    }

    fun getOnboardingStatus(): LiveData<Boolean> {
        return preferences.isOnboarded().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: BerkebunRepository? = null
        fun getInstance(
            preferences: SettingPreferences,
            apiService: ApiService
        ): BerkebunRepository =
            instance ?: synchronized(this) {
                instance ?: BerkebunRepository(preferences, apiService)
            }.also { instance = it }
    }
}