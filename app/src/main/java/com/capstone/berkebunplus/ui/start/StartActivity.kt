package com.capstone.berkebunplus.ui.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.databinding.ActivityStartBinding
import com.capstone.berkebunplus.ui.auth.login.LoginActivity

class StartActivity : AppCompatActivity() {
    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartBinding.inflate(layoutInflater)
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
        binding.buttonMasuk.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun playAnimation() {
        val imageStart = ObjectAnimator.ofFloat(binding.imageAvatar, View.ALPHA, 1f).setDuration(300)
        val titleStart = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(300)
        val titleDescription = ObjectAnimator.ofFloat(binding.textDescription, View.ALPHA, 1f).setDuration(300)
        val buttonStart = ObjectAnimator.ofFloat(binding.buttonMasuk, View.ALPHA, 1f).setDuration(300)
        val titleAgreement = ObjectAnimator.ofFloat(binding.textAgreement, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                imageStart,
                titleStart,
                titleDescription,
                buttonStart,
                titleAgreement
            )
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}