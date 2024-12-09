package com.capstone.berkebunplus.data

import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun loginWithEmail(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = firebaseAuth.currentUser
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun loginWithGoogle(idToken: String) = liveData {
        emit(Result.Loading)
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            val user = firebaseAuth.currentUser
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun registerUser(fullName: String, email: String, pass: String, confirmPass: String) = liveData {
        emit(Result.Loading)
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val user = firebaseAuth.currentUser
            user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()
            )?.await()

            if (user != null) {
                saveUserToFirestore(user.uid, fullName, email)
                emit(Result.Success(user))
            } else {
                emit(Result.Error("UID pengguna tidak valid!"))
            }



        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    private fun saveUserToFirestore(uid: String, fullName: String, email: String) {
        val fireStore = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "fullname" to fullName,
            "email" to email
        )

        fireStore.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener { /* Handle success */ }
            .addOnFailureListener { e -> /* Handle failure */ }
    }
}