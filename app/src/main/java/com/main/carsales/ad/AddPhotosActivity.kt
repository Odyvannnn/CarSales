package com.main.carsales.ad

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.main.carsales.R
import com.main.carsales.databinding.ActivityAddPhotosBinding
import com.main.carsales.main.MainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPhotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotosBinding
    private val storageRef = Firebase.storage.reference
    private val db = Firebase.firestore
    private var imageCapture: ImageCapture? = null
    private var imageCount = 0
    private var imageCountSent = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotosBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        requestCameraPermission(this)
        startCamera()
        loadHints(R.drawable.ph_hint_fr)
        setContentView(binding.root)
        binding.continueButton.visibility = View.INVISIBLE
        binding.imageCaptureButton.setOnClickListener {
            when (imageCount){
                0 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_left)
                    loadHints(R.drawable.ph_hint_left)
                }
                1 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_back)
                    loadHints(R.drawable.ph_hint_back)
                }
                2 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_right)
                    loadHints(R.drawable.ph_hint_right)
                }
                3 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_interior)
                    loadHints(R.drawable.ph_hint_interior)
                }
                4 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_str_wheel)
                    loadHints(R.drawable.ph_hint_str_wheel)
                }
                5 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_knob)
                    loadHints(R.drawable.ph_hint_knob)
                }
                6 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_driver_seat)
                    loadHints(R.drawable.ph_hint_driver_seat)
                }
                7 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_motor)
                    loadHints(R.drawable.ph_hint_motor)
                }
                8 -> {
                    binding.photoHintText.text = resources.getString(R.string.ph_end)
                    binding.imageViewHint.visibility = View.INVISIBLE
                }
            }
            takePhoto()
            imageCount++
            if (imageCount >= 9) {
                binding.imageCaptureButton.visibility = View.INVISIBLE
                binding.continueButton.visibility = View.VISIBLE
            }
        }
        binding.continueButton.setOnClickListener {
            val status = HashMap<String, Any>()
            status["status"] = "on_moderation"
            val fileName = intent.getStringExtra("fileName")
            if (fileName != null) {
                db.collection("ads").document(fileName).update(status)
            }
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Объявление отправлено на модерацию", Toast.LENGTH_SHORT).show()
        }

    }
    private fun requestCameraPermission(activity: AddPhotosActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile()

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                @SuppressLint("UnsafeExperimentalUsageError")
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    uploadImageToFirebaseStorage(savedUri)
                }
                override fun onError(exception: ImageCaptureException) {
                }
            })
    }

    private fun uploadImageToFirebaseStorage(uri: Uri) {
        val sd = System.currentTimeMillis().toString()
        val fileName = intent.getStringExtra("fileName")
        val uploadTask = storageRef.child("photos/$sd").putFile(uri)
        uploadTask.addOnSuccessListener {
            storageRef.child("photos/$sd").downloadUrl.addOnSuccessListener {
                val map = HashMap<String, Any>()
                map["pic$imageCountSent"] = it.toString()
                if (fileName != null) {
                    db.collection("ads").document(fileName).update(map)
                    Toast.makeText(this, "Фото загружено", Toast.LENGTH_SHORT).show()
                    imageCountSent++
                }
            }.addOnFailureListener {
            }
        }
    }

    private fun createFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = this.getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }

    private fun loadHints(uri: Int){
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .into(binding.imageViewHint)
    }

}