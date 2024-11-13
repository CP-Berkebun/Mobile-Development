package com.capstone.berkebunplus.ui.diagnosis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.berkebunplus.databinding.FragmentDiagnosisBinding

class DiagnosisFragment : Fragment() {

    private var _binding: FragmentDiagnosisBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val diagnosisViewModel =
            ViewModelProvider(this)[DiagnosisViewModel::class.java]

        _binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDiagnose
        diagnosisViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}