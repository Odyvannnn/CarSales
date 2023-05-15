package com.main.carsales.ad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.airbnb.paris.extensions.style
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.main.carsales.R
import com.main.carsales.adapters.GalleryAdapter
import com.main.carsales.databinding.ActivityAdBinding
import com.main.carsales.main.MainActivity
import com.main.carsales.messages.FirstMessageActivity

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val images = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setValuesToViews()

        val toId = intent.getStringExtra("seller_uid")
        val adId = intent.getStringExtra("adId")
        val fromId = auth.uid

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }
        binding.viewPager.adapter = GalleryAdapter(images)
        binding.viewPager.setPageTransformer(getTransformation())


        when (intent.getStringExtra("status")){
            "in_archive" ->{
                supportActionBar?.title = "Объявление находится в архиве"
                binding.writeOrDeleteButton.visibility = View.INVISIBLE
            }
            "on_moderation" ->{
                supportActionBar?.title = "Объявление на модерации"
                deleteButton()
            }
            "retake_photos" ->{
                supportActionBar?.title = "Переснимите автомобиль"
                deleteButton()
                binding.retakePhotosButton.visibility = View.VISIBLE
                binding.retakePhotosButton.setOnClickListener {
                    val fileName = intent.getStringExtra("adId")
                    if (fileName != null){
                        deletePhotos(fileName)
                        val intent = Intent(this, AddPhotosActivity::class.java)
                        intent.putExtra("fileName", fileName)
                        startActivity(intent)
                    }
                }
            }
            "published" -> {
                if (fromId == toId){
                    supportActionBar?.title = "Объявление активно"
                    deleteButton()
                }else{
                    binding.writeOrDeleteButton.text = "Написать продавцу"
                    binding.writeOrDeleteButton.setOnClickListener {
                        val intent = Intent(this, FirstMessageActivity::class.java)
                        intent.putExtra("seller_uid", toId)
                        intent.putExtra("adId", adId)
                        startActivity(intent) }
                }
            }
        }
    }

    private fun deleteButton(){
        binding.writeOrDeleteButton.style(R.style.CancelButtonTheme)
        binding.writeOrDeleteButton.text = "Удалить объявление"
        binding.writeOrDeleteButton.setOnClickListener {
            val fileName = intent.getStringExtra("adId")
            if (fileName != null) {
                archivePhotos(fileName)
                Toast.makeText(this, "Объявление удалено", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun archivePhotos(reference: String){
        val storage = FirebaseStorage.getInstance()
        val fileName = intent.getStringExtra("adId")
        val docRef = db.collection("ads").document(reference)
        val status = HashMap<String, Any>()
        status["status"] = "in_archive"
        docRef.get().addOnSuccessListener {
            val data = it.data
            if (data != null) {
                for (i in 2 until 10) {
                    val photoUrl = data["pic$i"].toString()
                    if (photoUrl != "null") {
                        val photoRef = storage.getReferenceFromUrl(photoUrl)
                        photoRef.delete().addOnSuccessListener {
                        }.addOnFailureListener {
                        }
                    }
                    else {
                        break
                    }
                }
                if (fileName != null) {
                    db.collection("ads").document(fileName).update(status)
                }
            }
        }.addOnFailureListener {
        }
    }

    private fun deletePhotos(reference: String){
        val storage = FirebaseStorage.getInstance()
        val docRef = db.collection("ads").document(reference)
        docRef.get().addOnSuccessListener {
            val data = it.data
            if (data != null) {
                for (i in 1 until 10) {
                    val photoUrl = data["pic$i"].toString()
                    if (photoUrl != "null") {
                        val photoRef = storage.getReferenceFromUrl(photoUrl)
                        photoRef.delete().addOnSuccessListener {
                            val updates = hashMapOf<String, Any>(
                                "pic$i" to FieldValue.delete()
                            )
                            docRef.update(updates)
                        }.addOnFailureListener {
                        }
                    }
                    else {
                        break
                    }
                }
            }
        }.addOnFailureListener {
        }
    }

    private fun getTransformation(): CompositePageTransformer{
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(30))
        transform.addTransformer{page, position ->
            page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
        }
        return transform
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setValuesToViews(){
        with(binding){
            carBrand.text = intent.getStringExtra("car_brand")
            carModel.text = intent.getStringExtra("car_model")
            price.text = intent.getStringExtra("price")
            year.text = intent.getStringExtra("year")
            infoYear.text = intent.getStringExtra("year")
            infoMileage.text = intent.getStringExtra("mileage")
            infoCarDriveType.text = intent.getStringExtra("car_drive_type")
            infoEnginePower.text = intent.getStringExtra("engine_power")
            infoEngineType.text = intent.getStringExtra("engine_type")
            infoTransmission.text = intent.getStringExtra("transmission")
            infoSellerCity.text = intent.getStringExtra("city")
            infoAd.text = intent.getStringExtra("info_ad")
        }
        for (i in 1..9){
            images.add(intent.getStringExtra("pic$i").toString())
        }
    }
}