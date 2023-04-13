package com.main.carsales.ad

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCameraPermission(this)
        binding.continueButton.visibility = View.INVISIBLE
        startCamera()
        binding.imageCaptureButton.setOnClickListener {
            when (imageCount){
                0 -> binding.photoHintText.text = resources.getString(R.string.ph_left)
                1 -> binding.photoHintText.text = resources.getString(R.string.ph_back)
                2 -> binding.photoHintText.text = resources.getString(R.string.ph_right)
                3 -> binding.photoHintText.text = resources.getString(R.string.ph_interior)
                4 -> binding.photoHintText.text = resources.getString(R.string.ph_str_wheel)
                5 -> binding.photoHintText.text = resources.getString(R.string.ph_knob)
                6 -> binding.photoHintText.text = resources.getString(R.string.ph_driver_seat)
                7 -> binding.photoHintText.text = resources.getString(R.string.ph_motor)
                8 -> binding.photoHintText.text = resources.getString(R.string.ph_end)
            }
            imageCount++
            takePhoto()
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
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun requestCameraPermission(activity: AddPhotosActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            Companion.CAMERA_PERMISSION_REQUEST_CODE
        )
    }
    fun handleCameraPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == Companion.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на использование камеры получено
                return true
            }
        }
        // Разрешение на использование камеры не получено
        return false
    }

    fun startCamera() {
        if (hasCameraPermission(this)) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            }, ContextCompat.getMainExecutor(this))
        } else {
            requestCameraPermission(this)
        }
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

    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile()

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                @SuppressLint("UnsafeExperimentalUsageError")
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Image saved successfully, upload to Firebase Storage
                    val savedUri = Uri.fromFile(photoFile)
                    uploadImageToFirebaseStorage(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    // Handle image capture errors
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
                map["pic$imageCount"] = it.toString()
                if (fileName != null) {
                    db.collection("ads").document(fileName).update(map)
                    Toast.makeText(this, "Фото сохранено", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                // Handle image upload failure
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

}
    /*private fun addPhoto(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        imagePickerActivityResult.launch(galleryIntent)
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                val imageUri: Uri? = result.data?.data
                val sd = System.currentTimeMillis().toString()
                val fileName = intent.getStringExtra("fileName")
                val uploadTask = imageUri?.let { storageRef.child("photos/$sd").putFile(it) }

                uploadTask?.addOnSuccessListener {
                    storageRef.child("photos/$sd").downloadUrl.addOnSuccessListener {
                        val map = HashMap<String, Any>()
                        map["pic$sd"] = it.toString()
                        if (fileName != null) {
                            db.collection("ads").document(fileName).update(map)
                        }
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }?.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }*/