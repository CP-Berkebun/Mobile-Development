@file:Suppress("DEPRECATION")

package com.capstone.berkebunplus.ui.auth.register
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.ActivityRegisterBinding
import com.capstone.berkebunplus.data.AuthViewModelFactory
import com.capstone.berkebunplus.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

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

    private fun setupAction() {
        binding.goLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.fieldFullName.text.toString()
            val email = binding.fieldEmail.text.toString()
            val pass = binding.fieldPassword.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()
            if (fullName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    registerUser(fullName, email, pass, confirmPass)
                } else {
                    Toast.makeText(this, "Password tidak sesuai!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun registerUser(fullName: String, email: String, pass: String, confirmPass: String) {
        viewModel.registerUser(fullName, email, pass, confirmPass).observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.set_title_success))
                        setMessage(getString(R.string.set_message_success_signUp))
                        setPositiveButton(getString(R.string.set_next)) { _, _ ->
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.set_title_error))
                        setMessage(result.message)
                        setPositiveButton(getString(R.string.error)) { _, _ ->
                        }
                        create()
                        show()
                    }
                }
                is Result.Loading -> { binding.progressIndicator.visibility = View.VISIBLE }
            }
        }
    }

    private fun playAnimation() {
        val imageStart =
            ObjectAnimator.ofFloat(binding.imageViewRegis, View.ALPHA, 1f).setDuration(300)
        val titleStart = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(300)
        val titleDescription =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val edtFullName =
            ObjectAnimator.ofFloat(binding.fieldFullName, View.ALPHA, 1f).setDuration(300)
        val edtEmail =
            ObjectAnimator.ofFloat(binding.fieldEmail, View.ALPHA, 1f).setDuration(300)
        val edtPassword =
            ObjectAnimator.ofFloat(binding.fieldPassword, View.ALPHA, 1f).setDuration(300)
        val edtConfirmPassword =
            ObjectAnimator.ofFloat(binding.confirmPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)
        val linearLayout1 =
            ObjectAnimator.ofFloat(binding.layout1, View.ALPHA, 1f).setDuration(300)
        val linearLayout2 =
            ObjectAnimator.ofFloat(binding.layout2, View.ALPHA, 1f).setDuration(300)
        val linearLayout3 =
            ObjectAnimator.ofFloat(binding.layout3, View.ALPHA, 1f).setDuration(300)

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