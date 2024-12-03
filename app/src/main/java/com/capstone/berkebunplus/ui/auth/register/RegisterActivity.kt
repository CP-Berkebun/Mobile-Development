@file:Suppress("DEPRECATION")

package com.capstone.berkebunplus.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.databinding.ActivityRegisterBinding
import com.capstone.berkebunplus.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.goLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val fullname = binding.fieldFullName.text.toString()
            val email = binding.fieldEmail.text.toString()
            val pass = binding.fieldPassword.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()

            registerViewModel.registerUser(fullname, email, pass, confirmPass)
        }
    }

    private fun observeViewModel() {
        registerViewModel.registerStatus.observe(this) { status ->
            when (status) {
                is RegisterViewModel.RegisterStatus.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is RegisterViewModel.RegisterStatus.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                    // Arahkan ke LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                is RegisterViewModel.RegisterStatus.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

