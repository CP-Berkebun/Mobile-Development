package com.capstone.berkebunplus.data.remote.retrofit

import com.capstone.berkebunplus.BuildConfig
import com.capstone.berkebunplus.BuildConfig.BASE_URL_WEATHER
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // Store API url in BuildConfigField first okay bro, then put in const variable
    private const val urlWeather = BASE_URL_WEATHER
    fun getWeatherApi(): ApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(urlWeather)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    // setup api config for another APIs here (in case we will put detection APIs)


}