@file:Suppress("DEPRECATION")

package com.capstone.berkebunplus.ui.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.MainActivity
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.ActivityLoginBinding
import com.capstone.berkebunplus.ui.auth.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Correct resource ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Navigate to RegisterActivity
        binding.goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Handle login with email and password
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            binding.progressIndicator.visibility = View.VISIBLE
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginViewModel.loginWithEmail(email, pass)
            } else {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(this, "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle login with Google
        binding.btnAuthGoogle.setOnClickListener {
            signInGoogle()
        }

        observeViewModel()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val idToken = account.idToken
                if (idToken != null) {
                    loginViewModel.loginWithGoogle(idToken)
                }
            }
        } else {
            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this) { result ->
            binding.progressIndicator.visibility = View.GONE
            result.onSuccess {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.googleSignInResult.observe(this) { result ->
            binding.progressIndicator.visibility = View.GONE
            result.onSuccess {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
