package com.capstone.berkebunplus.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.capstone.berkebunplus.databinding.FragmentThirdScreenBinding
import com.capstone.berkebunplus.ui.OnBoardingPreferences
import com.capstone.berkebunplus.ui.dataStore
import com.capstone.berkebunplus.ui.start.StartActivity
import kotlinx.coroutines.launch

class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFinish.setOnClickListener {
            lifecycleScope.launch {
                val preferences = OnBoardingPreferences(requireContext().dataStore)
                preferences.setOnboarded(true)

                val intent = Intent(requireActivity(), StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
