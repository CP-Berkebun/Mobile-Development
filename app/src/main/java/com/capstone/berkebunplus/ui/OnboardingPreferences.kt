package com.capstone.berkebunplus.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")
class OnBoardingPreferences (private val dataStore: DataStore<Preferences>){
    private val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")

    suspend fun setOnboarded(onboarded: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDED] = onboarded
        }
    }

    val isOnboarded: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_ONBOARDED] ?: false
        }
}