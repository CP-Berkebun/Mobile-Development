package com.capstone.berkebunplus.ui.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.berkebunplus.data.BerkebunRepository

class DiagnosisViewModel(private val repository: BerkebunRepository) : ViewModel() {
    fun getDiagnoses(userId: String) =
        repository.getDiagnoses(userId)
}