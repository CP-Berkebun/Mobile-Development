package com.capstone.berkebunplus.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val _googleSignInResult = MutableLiveData<Result<Boolean>>()
    val googleSignInResult: LiveData<Result<Boolean>> = _googleSignInResult

    fun loginWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.postValue(Result.success("Login berhasil"))
                } else {
                    _loginResult.postValue(Result.failure(task.exception ?: Exception("Login gagal")))
                }
            }
    }

    fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _googleSignInResult.postValue(Result.success(true))
                } else {
                    _googleSignInResult.postValue(Result.failure(task.exception ?: Exception("Google Sign-In gagal")))
                }
            }
    }
}
