package com.capstone.berkebunplus.ui.camera

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.createCustomTempFile
import com.capstone.berkebunplus.databinding.ActivityCameraBinding

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var popupDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.pickImage.setOnClickListener { startGallery() }
        binding.backToHome.setOnClickListener { finish() }
        binding.tips.setOnClickListener { showPopupDialog() }
        showPopupDialog()
//        // Inflate layout popup dari popup_layout.xml
//        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
//
//        // Tambahkan popup ke root layout di activity_camera.xml
//        val popupContainer = popupView.findViewById<LinearLayout>(R.id.popupContainer)
//        val btnUnderstand = popupView.findViewById<Button>(R.id.btnUnderstand)
//
//        // Tambahkan popup ke layout activity_camera.xml
//        val rootLayout = binding.root // root layout activity_camera.xml
//        (rootLayout as ViewGroup).addView(popupView)
//
//        // Menampilkan popup
//        showPopup(popupContainer)

        // Menambahkan listener untuk tombol Understand
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.viewFinder.surfaceProvider
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    setResult(CAMERAX_RESULT, intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly
        ))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val intent = Intent().apply {
                putExtra(EXTRA_GALLERY_IMAGE, uri.toString())
            }
            setResult(GALLERY_IMAGE_RESULT, intent)
            finish()
        } else {
            Toast.makeText(this, "Gagal memilih gambar.", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*" // Menampilkan hanya file gambar
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            val selectedImageUri = data?.data
//            if (selectedImageUri != null) {
//                // Menampilkan gambar yang dipilih di imageViewGallery
//                binding.pickImage.setImageURI(selectedImageUri)
//                binding.pickImage.visibility = View.VISIBLE
//                binding.viewFinder.visibility = View.GONE
//            } else {
//                Toast.makeText(this, "Gagal memilih gambar.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun hideSystemUI() {
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

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun showPopupDialog() {
        if (popupDialog == null) {
            popupDialog = Dialog(this).apply {
                setContentView(R.layout.popup_layout)
                setCancelable(true)
            }
        }
        popupDialog?.show()

        val understand = popupDialog!!.findViewById<Button>(R.id.btnUnderstand)
        understand.setOnClickListener {
            hidePopupDialog()
        }
    }

    private fun hidePopupDialog() {
        popupDialog?.let {
            if (it.isShowing) {
                it.dismiss() // Pastikan dialog ditutup
            }
        }
        popupDialog = null
    }

//    // Fungsi untuk menampilkan popup
//    private fun showPopup(popupContainer: LinearLayout) {
//        popupContainer.visibility = View.VISIBLE
//    }
//
//    // Fungsi untuk menyembunyikan popup
//    private fun hidePopup(popupContainer: LinearLayout) {
//        popupContainer.visibility = View.GONE
//    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val EXTRA_GALLERY_IMAGE = "Gallery Image"
        const val CAMERAX_RESULT = 200
        const val GALLERY_IMAGE_RESULT = 200
        const val RESULT_OK = 200
        private const val PICK_IMAGE_REQUEST = 1 // Tambahkan konstanta untuk request code
    }
}
