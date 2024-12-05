package com.capstone.berkebunplus.di

import android.content.Context
import com.capstone.berkebunplus.data.BerkebunRepository
import com.capstone.berkebunplus.data.local.datastore.SettingPreferences
import com.capstone.berkebunplus.data.local.datastore.dataStore
import com.capstone.berkebunplus.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): BerkebunRepository {
        val prefs = SettingPreferences.getInstance(context.dataStore)
        // This is API Config for Weather APIs
        val apiServiceWeather = ApiConfig.getWeatherApi()
        // Add another API Config in here (in case we will put detection APIs here)
        val apiServicePredict = ApiConfig.getPredictApi()
        return BerkebunRepository.getInstance(prefs, apiServiceWeather, apiServicePredict)
    }
}