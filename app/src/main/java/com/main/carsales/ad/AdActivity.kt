package com.main.carsales.ad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.carsales.databinding.ActivityAdBinding

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setValuesToViews()
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
    }
}