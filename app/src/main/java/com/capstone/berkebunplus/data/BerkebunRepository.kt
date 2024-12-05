package com.capstone.berkebunplus.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.capstone.berkebunplus.data.local.datastore.SettingPreferences
import com.capstone.berkebunplus.data.remote.response.DiagnosesItem
import com.capstone.berkebunplus.data.remote.response.SaveDiagnosesRequest
import com.capstone.berkebunplus.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BerkebunRepository(
    private val preferences: SettingPreferences,
    private val apiServiceWeather: ApiService,
    private val apiServicePredict: ApiService
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

    fun predictImage(imageFile: File, userId: String) = liveData {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        try {
            val response = apiServicePredict.predictImage(multipartBody, userId)
            Log.e("PredictImageSuccess", "Success predicting image: ${response.message}")
            emit(Result.Success(response))
        } catch (exc: Exception) {
            Log.e("PredictImageError", "Error predicting image: ${exc.message}")
            emit(Result.Error("${exc.message}"))
        }
    }

    fun saveDiagnoses(userId: String, data: SaveDiagnosesRequest) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServicePredict.saveDiagnoses(userId, data)
            Log.e("SaveDiagnosesSuccess", "Success save diagnoses: ${response.message}")
            emit(Result.Success(response))
        } catch (exc: Exception) {
            Log.e("SaveDiagnosesError", "Error save diagnoses: ${exc.message}")
            emit(Result.Error("${exc.message}"))
        }
    }

    fun getDiagnoses(userId: String) : LiveData<Result<List<DiagnosesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServicePredict.getDiagnoses(userId)
            val diagnoses = response.data?.diagnoses
            if (!diagnoses.isNullOrEmpty()) {
                Log.e("GetDiagnosesSuccess", "Success get diagnoses: ${response.message}")
                emit(Result.Success(diagnoses))
            } else {
                emit(Result.Error("No diagnoses data found"))
            }
        } catch (exc: Exception) {
            Log.e("GetDiagnosesError", "Error get diagnoses: ${exc.message}")
            emit(Result.Error("${exc.message}"))
        }
    }

    fun deleteDiagnoses(userId: String, diagnosedId: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServicePredict.deleteDiagnoses(userId, diagnosedId)
            Log.e("DeleteDiagnosesSuccess", "Success delete diagnoses: ${response.message}")
            emit(Result.Success(response.status))
        } catch (exc: Exception) {
            Log.e("DeleteDiagnosesError", "Error delete diagnoses: ${exc.message}")
            emit(Result.Error("${exc.message}"))
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
            apiServiceWeather: ApiService,
            apiServicePredict: ApiService
        ): BerkebunRepository =
            instance ?: synchronized(this) {
                instance ?: BerkebunRepository(preferences, apiServiceWeather, apiServicePredict)
            }.also { instance = it }
    }
}