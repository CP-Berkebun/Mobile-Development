package com.capstone.berkebunplus.ui.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.berkebunplus.MainActivity
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.ViewModelFactory
import com.capstone.berkebunplus.adapter.DiagnosesAdapter
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.ActivityDetailDiagnosisBinding
import com.capstone.berkebunplus.ui.diagnosis.DiagnosisViewModel
import com.capstone.berkebunplus.ui.resultscan.ResultScanActivity
import com.google.firebase.auth.FirebaseAuth

class DetailDiagnosisActivity : AppCompatActivity() {

    private var _binding: ActivityDetailDiagnosisBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiagnosisViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

        binding.btnRemoveToFavorite.setOnClickListener {
            deleteDiagnosesData()
        }
    }

    private fun deleteDiagnosesData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val diagnosedId = intent.getStringExtra(DiagnosesAdapter.DIAGNOSES_ID_EXTRA)?: return
        viewModel.deleteDiagnoses(userId, diagnosedId).observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.set_title_success))
                        setMessage("berhasil menghapus hasil diagnoses")
                        setPositiveButton(getString(R.string.set_next)) { _, _ ->
                            val intent = Intent(this@DetailDiagnosisActivity, MainActivity::class.java).apply {
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
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> { binding.progressIndicator.visibility = View.VISIBLE }
            }
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