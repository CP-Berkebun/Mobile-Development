package com.capstone.berkebunplus.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.berkebunplus.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun registerUser(fullName: String, email: String, password: String, confirmPassword: String) =
        authRepository.registerUser(fullName, email, password, confirmPassword)

}
