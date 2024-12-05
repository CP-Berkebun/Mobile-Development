package com.capstone.berkebunplus.ui.resultscan

import androidx.lifecycle.ViewModel
import com.capstone.berkebunplus.data.BerkebunRepository
import com.capstone.berkebunplus.data.remote.response.SaveDiagnosesRequest

class ResultScanViewModel(private val repository: BerkebunRepository): ViewModel() {
    fun saveDiagnoses(userId: String, data: SaveDiagnosesRequest) =
        repository.saveDiagnoses(userId, data)
}