package com.main.carsales.ad

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.main.carsales.adapters.GalleryAdapter
import com.main.carsales.databinding.ActivityAdBinding

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding
    private lateinit var galleryRecyclerView: RecyclerView
    private val images = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setValuesToViews()

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }
        binding.viewPager.adapter = GalleryAdapter(images)
        /*binding.galleryRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        setValuesToViews()

        val galleryAdapter = GalleryAdapter(images)
        binding.galleryRecyclerView.adapter = galleryAdapter*/

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setValuesToViews(){
        binding.carBrand.text = intent.getStringExtra("car_brand")
        binding.carModel.text = intent.getStringExtra("car_model")
        binding.price.text = intent.getStringExtra("price")
        binding.year.text = intent.getStringExtra("year")
        binding.infoYear.text = intent.getStringExtra("year")
        binding.infoMileage.text = intent.getStringExtra("mileage")
        binding.infoCarDriveType.text = intent.getStringExtra("car_drive_type")
        binding.infoEnginePower.text = intent.getStringExtra("engine_power")
        binding.infoTransmission.text = intent.getStringExtra("transmission")
        for (i in 1..9){
            images.add(intent.getStringExtra("pic$i").toString())
        }
    }

    private fun loadPhoto(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

}