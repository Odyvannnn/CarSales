package com.main.carsales.ad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.main.carsales.adapters.GalleryAdapter
import com.main.carsales.databinding.ActivityAdBinding
import com.main.carsales.messages.ChatActivity

class AdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdBinding
    private val images = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setValuesToViews()

        val toId = intent.getStringExtra("seller_uid")

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }
        binding.viewPager.adapter = GalleryAdapter(images)
        binding.writeToSellerButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("seller_uid", toId)
            startActivity(intent)
        }
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
        }
        for (i in 1..9){
            images.add(intent.getStringExtra("pic$i").toString())
        }
    }
}