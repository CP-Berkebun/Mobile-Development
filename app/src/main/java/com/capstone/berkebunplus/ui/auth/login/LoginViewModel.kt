package com.capstone.berkebunplus.ui.auth.login

import androidx.lifecycle.ViewModel
import com.capstone.berkebunplus.data.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun loginWithEmail(email: String, password: String) = authRepository.loginWithEmail(email, password)

    fun loginWithGoogle(idToken: String) = authRepository.loginWithGoogle(idToken)
}
