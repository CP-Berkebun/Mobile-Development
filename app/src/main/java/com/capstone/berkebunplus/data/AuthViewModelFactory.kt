package com.capstone.berkebunplus.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.berkebunplus.data.AuthRepository
import com.capstone.berkebunplus.ui.auth.login.LoginViewModel
import com.capstone.berkebunplus.ui.auth.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory private constructor(private val authRepository: AuthRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            // assign view model here okay
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(): AuthViewModelFactory {
            val authRepository = AuthRepository(FirebaseAuth.getInstance())
            return AuthViewModelFactory(authRepository)
        }
    }
}