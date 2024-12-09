package com.capstone.berkebunplus.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.Manifest
import android.app.Dialog
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.ViewModelFactory
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.FragmentHomeBinding
import com.capstone.berkebunplus.reduceFileImage
import com.capstone.berkebunplus.ui.camera.CameraActivity
import com.capstone.berkebunplus.ui.resultscan.ResultScanActivity
import com.capstone.berkebunplus.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.Q)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var loadingDialog: Dialog? = null
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraX()
                Toast.makeText(requireContext(), "Izin kamera diizinkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
                Toast.makeText(requireContext(), "Izin lokasi diizinkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

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
        } else {
            getCurrentLocation()
        }

        binding.btnScanImage.setOnClickListener {
            if (!isCameraPermissionGranted()) {
                requestCameraPermissionLauncher.launch(REQUIRED_PERMISSION_CAMERA)
            } else {
                startCameraX()
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

        if (it.resultCode == CameraActivity.RESULT_OK) {
            val data = it.data
            val galleryImageUri = data?.getStringExtra(CameraActivity.EXTRA_GALLERY_IMAGE)?.toUri()
            val cameraImageUri = data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()

            currentImageUri = galleryImageUri ?: cameraImageUri

            if (currentImageUri != null) {
                uploadImage()
            } else {
                Toast.makeText(requireContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImage() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            viewModel.predictImage(imageFile, userId).observe(viewLifecycleOwner) { result ->
                when(result) {
                    is Result.Success -> {
                        hideLoadingDialog()
                        val intent = Intent(requireContext(), ResultScanActivity::class.java).apply {
                            val response = result.data.data
                            putExtra(ResultScanActivity.USER_ID_EXTRA, userId)
                            putExtra(ResultScanActivity.DIAGNOSES_ID_EXTRA, response?.diagnosedId)
                            putExtra(ResultScanActivity.IMAGE_EXTRA, response?.imageUrl)
                            putExtra(ResultScanActivity.PLANT_EXTRA, response?.diagnoses?.plant)
                            putExtra(ResultScanActivity.TUMBUHAN_EXTRA, response?.diagnoses?.tumbuhan)
                            putExtra(ResultScanActivity.DISEASE_ID_EXTRA, response?.diagnoses?.penyakitId)
                            putExtra(ResultScanActivity.DESCRIPTION_EXTRA, response?.diagnoses?.deskripsi)
                            putExtra(ResultScanActivity.TREATMENT_EXTRA, response?.diagnoses?.treatment)
                        }
                        startActivity(intent)
                    }
                    is Result.Error -> {
                        hideLoadingDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoadingDialog()
                    }
                }
            }
        }
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(requireContext()).apply {
                setContentView(R.layout.custom_loading_dialog)
                setCancelable(false)
            }
        }
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        loadingDialog = null
    }


    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RESULT_OK = 1
        private const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val REQUIRED_PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}