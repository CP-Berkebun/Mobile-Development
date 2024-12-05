@file:Suppress("DEPRECATION")

package com.capstone.berkebunplus.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
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
import com.google.firebase.auth.GoogleAuthProvider
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        
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

        binding.btnAuthGoogle.setOnClickListener {
            binding.progressIndicator.visibility = View.VISIBLE
            signInGoogle()
        }

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

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
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
// =======
//     private fun updateUI(account: GoogleSignInAccount) {
//         val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//         firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
//             if (task.isSuccessful) {
//                 showSuccessDialog(account)
//             } else {
//                 Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
// >>>>>>> main
            }
        }
    }

//     private fun showSuccessDialog(account: GoogleSignInAccount) {
//         AlertDialog.Builder(this).apply {
//             setTitle(getString(R.string.set_title_success))
//             setMessage(getString(R.string.set_message_success))
//             setPositiveButton(getString(R.string.set_next)) { _, _ ->
//                 val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
//                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                     putExtra("email", account.email)
//                     putExtra("name", account.displayName)
//                 }
//                 startActivity(intent)
//                 finish()
//             }
//             create()
//             show()
//         }
//     }

    private fun playAnimation() {
        val imageStart = ObjectAnimator.ofFloat(binding.imageViewLogin, View.ALPHA, 1f).setDuration(300)
        val titleStart = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(300)
        val titleDescription = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val edtEmail = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(300)
        val edtPassword = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)
        val linearLayout1 = ObjectAnimator.ofFloat(binding.layout1, View.ALPHA, 1f).setDuration(300)
        val linearLayout2 = ObjectAnimator.ofFloat(binding.layout2, View.ALPHA, 1f).setDuration(300)
        val linearLayout3 = ObjectAnimator.ofFloat(binding.layout3, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                imageStart,
                titleStart,
                titleDescription,
                edtEmail,
                edtPassword,
                btnLogin,
                linearLayout1,
                linearLayout2,
                linearLayout3
            )
            start()
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
