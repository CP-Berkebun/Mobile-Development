package com.capstone.berkebunplus.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.berkebunplus.databinding.ActivityAboutAppBinding


class AboutAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}