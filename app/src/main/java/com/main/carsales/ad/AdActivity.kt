package com.main.carsales.ad

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.main.carsales.databinding.ActivityAdBinding

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding
    private var url1 = ""
    private var url2 = ""
    private var url3 = ""
    private var url4 = ""
    private var url5 = ""
    private var url6 = ""
    private var url7 = ""
    private var url8 = ""
    private var url9 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setValuesToViews()
        loadPhoto(url1, binding.imageViewFr)
        loadPhoto(url2, binding.imageViewLeft)
        loadPhoto(url3, binding.imageViewBack)
        loadPhoto(url4, binding.imageViewRight)
        loadPhoto(url5, binding.imageViewInterior)
        loadPhoto(url6, binding.imageViewStrWheel)
        loadPhoto(url7, binding.imageViewKnob)
        loadPhoto(url8, binding.imageViewDriverSeat)
        loadPhoto(url9, binding.imageViewMotor)
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
        url1 = intent.getStringExtra("pic1").toString()
        url2 = intent.getStringExtra("pic2").toString()
        url3 = intent.getStringExtra("pic3").toString()
        url4 = intent.getStringExtra("pic4").toString()
        url5 = intent.getStringExtra("pic5").toString()
        url6 = intent.getStringExtra("pic6").toString()
        url7 = intent.getStringExtra("pic7").toString()
        url8 = intent.getStringExtra("pic8").toString()
        url9 = intent.getStringExtra("pic9").toString()
    }

    private fun loadPhoto(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

}