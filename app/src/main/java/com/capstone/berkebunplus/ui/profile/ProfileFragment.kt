package com.capstone.berkebunplus.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.FragmentProfileBinding
import com.capstone.berkebunplus.ui.auth.login.LoginActivity
import android.provider.Settings
import com.capstone.berkebunplus.ui.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        // Set tombol logout
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut() // Logout dari Firebase
            val intent = Intent(requireContext(), StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnAboutApp.setOnClickListener {
            val intent = Intent(requireContext(), AboutAppActivity::class.java)
            startActivity(intent)
        }

        binding.btnInformasiDetail.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLanguage.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }


        loadUserProfile()

        return root
    }

    override fun onStart() {
        super.onStart()
        // Cek apakah user sudah login
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadUserProfile() {
        val user: FirebaseUser? = firebaseAuth.currentUser

        if (user != null) {
            // Set nama pengguna
            binding.yourName.text = user.displayName ?: getString(R.string.default_user_name)

            // Set email pengguna
            binding.email.text = user.email ?: getString(R.string.default_user_email)

            // Set foto profil
            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .into(binding.avatar)
            } else {
                binding.avatar.setImageResource(R.drawable.ic__account_circle_24)
            }
        } else {
            Toast.makeText(context, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


