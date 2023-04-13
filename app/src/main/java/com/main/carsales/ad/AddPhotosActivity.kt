package com.main.carsales.ad

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.main.carsales.databinding.ActivityAddPhotosBinding

class AddPhotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotosBinding
    private val storageRef = Firebase.storage.reference
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontPhButton.setOnClickListener{
            addPhoto()
        }
        binding.leftPhButton.setOnClickListener{
            addPhoto()
        }

    }
    private fun addPhoto(){
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
        }


}