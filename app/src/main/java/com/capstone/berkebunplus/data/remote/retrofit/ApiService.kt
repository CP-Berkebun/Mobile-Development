package com.capstone.berkebunplus.data.remote.retrofit

import com.capstone.berkebunplus.BuildConfig
import com.capstone.berkebunplus.data.remote.response.DiagnosesResponse
import com.capstone.berkebunplus.data.remote.response.PredictResponse
import com.capstone.berkebunplus.data.remote.response.SaveDiagnosesRequest
import com.capstone.berkebunplus.data.remote.response.SaveDiagnosesResponse
import com.capstone.berkebunplus.data.remote.response.WeatherResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    // setup api service and put another endpoint API here (in case we will put detection APIs endpoint)
    @Multipart
    @POST("diagnoses")
    suspend fun predictImage(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: String
    ): PredictResponse

    @POST("diagnoses/{userId}/save")
    suspend fun saveDiagnoses(
        @Path("userId") userId: String,
        @Body body: SaveDiagnosesRequest
    ) : SaveDiagnosesResponse

    @GET("diagnoses/{userId}")
    suspend fun getDiagnoses(
        @Path("userId") userId: String
    ) : DiagnosesResponse

}