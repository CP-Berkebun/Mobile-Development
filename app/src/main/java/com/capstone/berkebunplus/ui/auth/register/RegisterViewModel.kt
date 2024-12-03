package com.capstone.berkebunplus.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    sealed class RegisterStatus {
        object Loading : RegisterStatus()
        object Success : RegisterStatus()
        class Error(val message: String) : RegisterStatus()
    }

    private val _registerStatus = MutableLiveData<RegisterStatus>()
    val registerStatus: LiveData<RegisterStatus> get() = _registerStatus

    fun registerUser(fullname: String, email: String, password: String, confirmPassword: String) {
        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _registerStatus.value = RegisterStatus.Error("Kolom tidak boleh kosong!")
            return
        }

        if (password != confirmPassword) {
            _registerStatus.value = RegisterStatus.Error("Kata sandi tidak cocok!")
            return
        }

        _registerStatus.value = RegisterStatus.Loading

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullname)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            saveUserToFirestore(user.uid, fullname, email)
                        } else {
                            _registerStatus.value = RegisterStatus.Error("Gagal memperbarui profil: ${updateTask.exception?.message}")
                        }
                    }
                } else {
                    _registerStatus.value = RegisterStatus.Error("Registrasi gagal: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserToFirestore(uid: String?, fullname: String, email: String) {
        if (uid == null) {
            _registerStatus.value = RegisterStatus.Error("UID pengguna tidak valid!")
            return
        }

        val firestore = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "fullname" to fullname,
            "email" to email
        )

        firestore.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                _registerStatus.value = RegisterStatus.Success
            }
            .addOnFailureListener { e ->
                _registerStatus.value = RegisterStatus.Error("Gagal menyimpan data ke Firestore: ${e.message}")
            }
    }
}
