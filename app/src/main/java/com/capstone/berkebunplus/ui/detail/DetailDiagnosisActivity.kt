package com.capstone.berkebunplus.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.adapter.DiagnosesAdapter
import com.capstone.berkebunplus.databinding.ActivityDetailDiagnosisBinding
import com.capstone.berkebunplus.ui.resultscan.ResultScanActivity

class DetailDiagnosisActivity : AppCompatActivity() {

    private var _binding: ActivityDetailDiagnosisBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailDiagnosisBinding.inflate(layoutInflater)
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
        binding.btnBackResult.setOnClickListener {
            finish()
        }
    }

    private fun showDiagnosesData() {
        val imageData = intent.getStringExtra(DiagnosesAdapter.IMAGE_EXTRA)
        val tumbuhanData = intent.getStringExtra(DiagnosesAdapter.TUMBUHAN_EXTRA)
        val diseaseIdData = intent.getStringExtra(DiagnosesAdapter.DISEASE_ID_EXTRA)
        val descriptionData = intent.getStringExtra(DiagnosesAdapter.DESCRIPTION_EXTRA)
        val treatmentData = intent.getStringExtra(DiagnosesAdapter.TREATMENT_EXTRA)

        Glide.with(this@DetailDiagnosisActivity)
            .load(imageData)
            .into(binding.ivDiagnosisResultImage)

        binding.tvResultPlantName.text = tumbuhanData
        binding.tvResultDiagnosisName.text = diseaseIdData
        binding.tvDiagnosisDescription.text = descriptionData
        binding.tvDiagnosisTreatment.text = treatmentData
    }


}