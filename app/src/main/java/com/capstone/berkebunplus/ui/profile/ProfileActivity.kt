package com.capstone.berkebunplus.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.ActivityProfileBinding
import com.capstone.berkebunplus.ui.auth.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        loadUserProfile()

        // Disable email input field
        binding.emailInput.isEnabled = false
        // Optional: Make email field read-only
        binding.emailInput.keyListener = null

        binding.btnBackProfile.setOnClickListener{

        }

        // Tombol Simpan Profil
        binding.editProfile.setOnClickListener {
            saveUserProfile()
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadUserProfile() {
        val user: FirebaseUser? = firebaseAuth.currentUser

        if (user != null) {
            binding.nameInput.setText(user.displayName ?: "")
            binding.emailInput.setText(user.email ?: "")

            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .into(binding.avatarImage)
            } else {
                binding.avatarImage.setImageResource(R.drawable.ic__account_circle_24)
            }
        } else {
            Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserProfile() {
        val user = firebaseAuth.currentUser
        if (user == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val newName = binding.nameInput.text.toString().trim()
        val newEmail = binding.emailInput.text.toString().trim()
        val newPassword = binding.passwordInput.text.toString().trim()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .setPhotoUri(user.photoUrl) // Tidak mengganti foto
            .build()

        // Update Display Name
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                }
            }

        // Update Email jika berbeda
        if (newEmail.isNotEmpty() && newEmail != user.email) {
            user.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Gagal memperbarui email", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Update Password jika ada
        if (newPassword.isNotEmpty()) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Kata sandi diperbarui", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
