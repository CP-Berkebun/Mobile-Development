package com.capstone.berkebunplus.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        binding.btnNext.setOnClickListener {
            val current = viewPager?.currentItem ?: 0
            viewPager?.currentItem = current + 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
