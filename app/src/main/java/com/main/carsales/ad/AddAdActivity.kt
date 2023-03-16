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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.databinding.ActivityAddAdBinding
import com.main.carsales.main.MainActivity


class AddAdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAdBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.publishButton.setOnClickListener{
            val adInfo = hashMapOf(
                "car_brand" to binding.carBrand.selectedItem.toString(),
                "car_model" to binding.carModel.selectedItem.toString(),
                "year" to binding.year.text.toString(),
                "mileage" to binding.mileage.text.toString(),
                "engine_power" to binding.enginePower.text.toString(),
                "transmission" to binding.transmission.selectedItem.toString(),
                "car_drive_type" to binding.carDriveType.selectedItem.toString(),
                "price" to binding.price.text.toString(),
                "status" to "on_moderation"
            )
            if (!validateForm()) {
                return@setOnClickListener
            }
            db.collection("ads").document().set(adInfo)
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(
                        this, "Объявление отправлено на модерацию",
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

        fun adapterSpinner(array: Array<String>, spinner: Spinner){
            val adapterSpinner =
                ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, array)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner
        }

        adapterSpinner(resources.getStringArray(R.array.car_brands), spinnerBrand)
        adapterSpinner(resources.getStringArray(R.array.car_transmission_array), spinnerTransmission)
        adapterSpinner(resources.getStringArray(R.array.car_drive_type_array), spinnerCarDriveType)

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
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val year = binding.year.text.toString()
        if (TextUtils.isEmpty(year)) {
            binding.year.error = "Введите год выпуска автомобиля."
            valid = false
        } else {
            binding.year.error = null
        }
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

}