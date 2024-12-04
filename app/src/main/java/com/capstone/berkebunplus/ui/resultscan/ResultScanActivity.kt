package com.capstone.berkebunplus.ui.resultscan

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.ActivityResultScanBinding
import com.capstone.berkebunplus.ui.diagnosis.DiagnosisFragment
import com.capstone.berkebunplus.ui.home.HomeFragment

@RequiresApi(Build.VERSION_CODES.Q)
class ResultScanActivity : AppCompatActivity() {

    private var _binding: ActivityResultScanBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        showDiagnosesData()
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
        binding.btnAddToFavorite.setOnClickListener { saveResult() }

        binding.btnBackResult.setOnClickListener {
            val intent = Intent(this@ResultScanActivity, HomeFragment::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun saveResult() {
        val intent = Intent(this@ResultScanActivity, DiagnosisFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showDiagnosesData() {
        val imageData = intent.getStringExtra(IMAGE_EXTRA)
        val plantData = intent.getStringExtra(PLANT_EXTRA)
        val tumbuhanData = intent.getStringExtra(TUMBUHAN_EXTRA)
        val diseaseIdData = intent.getStringExtra(DISEASE_ID_EXTRA)
        val descriptionData = intent.getStringExtra(DESCRIPTION_EXTRA)
        val treatmentData = intent.getStringExtra(TREATMENT_EXTRA)

        Glide.with(this@ResultScanActivity)
            .load(imageData)
            .into(binding.imageView)

        binding.tvResultPlantName.text = tumbuhanData
        binding.tvResultDiagnosisName.text = diseaseIdData
        binding.tvDiagnosisDescription.text = descriptionData
        binding.tvDiagnosisTreatment.text = treatmentData
    }

    companion object {
        const val USER_ID_EXTRA = "userId_extra"
        const val IMAGE_EXTRA = "image_extra"
        const val PLANT_EXTRA = "plant_extra"
        const val TUMBUHAN_EXTRA = "tumbuhan_extra"
        const val DISEASE_ID_EXTRA = "disease_extra"
        const val DESCRIPTION_EXTRA = "description_extra"
        const val TREATMENT_EXTRA = "treatment_extra"
    }
}