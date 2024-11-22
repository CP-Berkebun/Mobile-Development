package com.capstone.berkebunplus.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.berkebunplus.databinding.FragmentThirdScreenBinding
import com.capstone.berkebunplus.data.local.datastore.SettingPreferences
import com.capstone.berkebunplus.data.local.datastore.SettingViewModel
import com.capstone.berkebunplus.data.local.datastore.SettingViewModelFactory
import com.capstone.berkebunplus.data.local.datastore.dataStore
import com.capstone.berkebunplus.ui.start.StartActivity
import kotlinx.coroutines.launch

class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(requireContext())
    }

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
            viewModel.setOnboarded(true)
                val intent = Intent(requireActivity(), StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
