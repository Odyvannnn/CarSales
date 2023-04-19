package com.main.carsales.ad

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.main.carsales.adapters.GalleryAdapter
import com.main.carsales.databinding.ActivityAdBinding
import com.main.carsales.messages.FirstMessageActivity

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding
    private lateinit var auth: FirebaseAuth
    private val images = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setValuesToViews()

        val toId = intent.getStringExtra("seller_uid")
        val fromId = auth.uid

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }

        binding.viewPager.adapter = GalleryAdapter(images)
        if (fromId == toId){
            binding.writeToSellerButton.visibility = View.INVISIBLE
        }
        binding.viewPager.setPageTransformer(getTransformation())

        binding.writeToSellerButton.setOnClickListener {
            val intent = Intent(this, FirstMessageActivity::class.java)
            intent.putExtra("seller_uid", toId)
            startActivity(intent)
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
            infoTransmission.text = intent.getStringExtra("transmission")
            infoSellerCity.text = intent.getStringExtra("city")
        }
        for (i in 1..9){
            images.add(intent.getStringExtra("pic$i").toString())
        }
    }
}