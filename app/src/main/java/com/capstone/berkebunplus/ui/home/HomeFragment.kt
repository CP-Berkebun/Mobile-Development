package com.capstone.berkebunplus.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.FragmentHomeBinding
import com.capstone.berkebunplus.ui.camera.CameraActivity
import com.capstone.berkebunplus.ui.camera.CameraActivity.Companion.CAMERAX_RESULT

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this@HomeFragment.requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory = HomeViewModelFactory.getInstance()
        val viewModel: HomeViewModel by viewModels { factory }

        viewModel.weatherData.observe(viewLifecycleOwner) { results->
            when (results) {
                is Result.Loading -> { binding.progressIndicator.visibility = View.VISIBLE}
                is Result.Success -> {
                    val weather = results.data
                    binding.progressIndicator.visibility = View.GONE
                    binding.tvCity.text = getString(R.string.result_info_city_country, weather.name, weather.sys.country)
                    binding.tvInfoDescriptionWeather.text = weather.weather[0].description
                    binding.tvInfoHumidityPercentage.text = getString(R.string.result_info_humidity, weather.main.humidity)
                    binding.tvInfoWindSpeed.text = getString(R.string.result_info_wind_speed, weather.wind.speed)
                    binding.tvInfoTemperature.text = getString(R.string.result_info_temperature, weather.main.temp)
                    binding.tvInfoPressure.text = getString(R.string.result_info_pressure, weather.main.pressure)
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                }
            }
        }
        viewModel.fetchWeather()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnScanImage.setOnClickListener { startCameraX() }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}