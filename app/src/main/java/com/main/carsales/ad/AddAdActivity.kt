package com.main.carsales.ad

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.databinding.ActivityAddAdBinding


class AddAdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAdBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.publishButton.setOnClickListener{
            val fileName = System.currentTimeMillis().toString()
            val adInfo = hashMapOf(
                "car_brand" to binding.carBrand.selectedItem.toString(),
                "car_model" to binding.carModel.selectedItem.toString(),
                "year" to binding.year.selectedItem.toString(),
                "mileage" to binding.mileage.text.toString(),
                "engine_power" to binding.enginePower.text.toString(),
                "transmission" to binding.transmission.selectedItem.toString(),
                "car_drive_type" to binding.carDriveType.selectedItem.toString(),
                "price" to binding.price.text.toString(),
                "city" to binding.city.selectedItem.toString(),
                "seller_uid" to auth.currentUser?.uid.toString(),
                "status" to "waiting_photos"
            )
            adInfo["mileage"] += " км"
            adInfo["engine_power"] += " л.с."
            adInfo["price"] += " ₽"
            if (!validateForm()) {
                return@setOnClickListener
            }
            db.collection("ads").document(fileName).set(adInfo)
                .addOnSuccessListener {
                    val intent = Intent(this, AddPhotosActivity::class.java)
                    intent.putExtra("fileName", fileName)
                    startActivity(intent)
                    Toast.makeText(
                        this, "Объявление сохранено",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this, "Ошибка, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        val spinnerBrand = findViewById<Spinner>(R.id.car_brand)
        val spinnerModel = findViewById<Spinner>(R.id.car_model)
        val spinnerTransmission = findViewById<Spinner>(R.id.transmission)
        val spinnerCarDriveType = findViewById<Spinner>(R.id.car_drive_type)
        val spinnerYear = findViewById<Spinner>(R.id.year)
        val spinnerCity = findViewById<Spinner>(R.id.city)

        fun adapterSpinner(array: Array<String>, spinner: Spinner){
            val adapterSpinner =
                ArrayAdapter(applicationContext, R.layout.spinner_item, array)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner
        }

        adapterSpinner(resources.getStringArray(R.array.car_brands), spinnerBrand)
        adapterSpinner(resources.getStringArray(R.array.car_transmission_array), spinnerTransmission)
        adapterSpinner(resources.getStringArray(R.array.car_drive_type_array), spinnerCarDriveType)
        adapterSpinner(resources.getStringArray(R.array.year_array), spinnerYear)
        adapterSpinner(resources.getStringArray(R.array.cities_array), spinnerCity)

        binding.carBrand.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position){
                    0 -> {
                        adapterSpinner(resources.getStringArray(R.array.audi_models), spinnerModel)
                    }
                    1 -> {
                        adapterSpinner(resources.getStringArray(R.array.bmw_models), spinnerModel)
                    }
                    2 -> {
                        adapterSpinner(resources.getStringArray(R.array.toyota_models), spinnerModel)
                    }
                    3 -> {
                        adapterSpinner(resources.getStringArray(R.array.year_array), spinnerYear)
                    }
                    4 -> {
                        adapterSpinner(resources.getStringArray(R.array.cities_array), spinnerCity)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val mileage = binding.mileage.text.toString()
        if (TextUtils.isEmpty(mileage)) {
            binding.mileage.error = "Введите пробег автомобиля."
            valid = false
        } else {
            binding.mileage.error = null
        }
        val enginePower = binding.enginePower.text.toString()
        if (TextUtils.isEmpty(enginePower)) {
            binding.enginePower.error = "Введите мощность двигателя."
            valid = false
        } else {
            binding.mileage.error = null
        }
        val price = binding.price.text.toString()
        if (TextUtils.isEmpty(price)) {
            binding.price.error = "Введите стоимость автомобиля."
            valid = false
        } else {
            binding.price.error = null
        }
        return valid
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}