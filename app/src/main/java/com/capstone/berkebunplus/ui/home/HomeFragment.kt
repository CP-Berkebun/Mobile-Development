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
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.ViewModelFactory
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.FragmentHomeBinding
import com.capstone.berkebunplus.ui.camera.CameraActivity
import com.capstone.berkebunplus.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraX()
            } else {
                Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
        requireContext(),
        REQUIRED_PERMISSION_CAMERA
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
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (!isLocationPermissionGranted()) {
            requestLocationPermissionLauncher.launch(REQUIRED_PERMISSION_LOCATION)
        }

        binding.btnScanImage.setOnClickListener {
            if (!isCameraPermissionGranted()) {
                requestCameraPermissionLauncher.launch(REQUIRED_PERMISSION_CAMERA)
            }
        }
    }

    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    observeWeatherData(it.latitude, it.longitude)
                } ?: run {
                    Toast.makeText(requireContext(), "Lokasi tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Gagal mendapatkan lokasi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Tidak dapat mengakses lokasi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeWeatherData(latitude: Double, longitude: Double) {
        viewModel.fetchWeather(latitude, longitude).observe(viewLifecycleOwner) { results ->
            when (results) {
                is Result.Loading -> binding.progressIndicator.visibility = View.VISIBLE
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
                    Toast.makeText(requireContext(), "Gagal memuat data cuaca: ${results.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Memulai CameraX
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

    companion object {
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val REQUIRED_PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}
