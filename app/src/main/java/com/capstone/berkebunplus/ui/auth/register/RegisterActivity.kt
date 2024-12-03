@file:Suppress("DEPRECATION")

package com.capstone.berkebunplus.ui.auth.register

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.MainActivity
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.ActivityRegisterBinding
import com.capstone.berkebunplus.ui.auth.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Correct resource ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnAuthGoogle.setOnClickListener {
            binding.progressIndicator.visibility = View.VISIBLE
            signInGoogle()
        }

        binding.goLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val fullname = binding.fieldFullName.text.toString()
            val email = binding.fieldEmail.text.toString()
            val pass = binding.fieldPassword.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()

            if (fullname.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Kolom tidak boleh kosong !!", Toast.LENGTH_SHORT).show()

            }
        }
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

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showSuccessDialog(account)
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialog(account: GoogleSignInAccount) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.set_title_success))
            setMessage(getString(R.string.set_message_success))
            setPositiveButton(getString(R.string.set_next)) { _, _ ->
                val intent = Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("email", account.email)
                    putExtra("name", account.displayName)
                }
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        val imageStart = ObjectAnimator.ofFloat(binding.imageViewRegis, View.ALPHA, 1f).setDuration(300)
        val titleStart = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(300)
        val titleDescription =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val edtFullName =
            ObjectAnimator.ofFloat(binding.fieldFullName, View.ALPHA, 1f).setDuration(300)
        val edtEmail = ObjectAnimator.ofFloat(binding.fieldEmail, View.ALPHA, 1f).setDuration(300)
        val edtPassword =
            ObjectAnimator.ofFloat(binding.fieldPassword, View.ALPHA, 1f).setDuration(300)
        val edtConfirmPassword =
            ObjectAnimator.ofFloat(binding.confirmPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)
        val linearLayout1 = ObjectAnimator.ofFloat(binding.layout1, View.ALPHA, 1f).setDuration(300)
        val linearLayout2 = ObjectAnimator.ofFloat(binding.layout2, View.ALPHA, 1f).setDuration(300)
        val linearLayout3 = ObjectAnimator.ofFloat(binding.layout3, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                imageStart,
                titleStart,
                titleDescription,
                edtFullName,
                edtEmail,
                edtPassword,
                edtConfirmPassword,
                btnRegister,
                linearLayout1,
                linearLayout2,
                linearLayout3
            )
            start()
        }
    }
}