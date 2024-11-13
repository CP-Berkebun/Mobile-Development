package com.capstone.berkebunplus.data.remote.retrofit

import com.capstone.berkebunplus.BuildConfig
import com.capstone.berkebunplus.data.remote.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String = "Jakarta",
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}