package com.capstone.berkebunplus.ui.diagnosis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.berkebunplus.ViewModelFactory
import com.capstone.berkebunplus.adapter.DiagnosesAdapter
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.FragmentDiagnosisBinding
import com.google.firebase.auth.FirebaseAuth

class DiagnosisFragment : Fragment() {

    private var _binding: FragmentDiagnosisBinding? = null
    private val binding get() = _binding!!
    private lateinit var diagnosesAdapter: DiagnosesAdapter

    private val viewModel: DiagnosisViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            showDiagnosesData()
        }, 1000)
    }

    private fun showDiagnosesData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.getDiagnoses(userId).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                    } else {
                        binding.empty.visibility = View.GONE
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        diagnosesAdapter = DiagnosesAdapter()
                        diagnosesAdapter.submitList(result.data)
                        binding.rvDiagnoses.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvDiagnoses.adapter = diagnosesAdapter
                        Log.d("diagnosesSuccess", "SuccessDiagnosesFragment")
                    }
                }
                is Result.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Gagal ambil data ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Loading -> {
                    binding.shimmerViewContainer.startShimmer()
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                }
            }
        }
    }

//    private fun showNoDataView(isEmpty: Boolean){
//        if (isEmpty) {
//            binding.empty.visibility = View.VISIBLE
//        } else {
//            binding.empty.visibility = View.GONE
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}