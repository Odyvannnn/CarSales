package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.ad.AdsListActivity
import com.main.carsales.auth.AuthActivity
import com.main.carsales.data.Ad
import com.main.carsales.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adList: ArrayList<Ad>
    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        verifyIsUserLoggedIn()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.searchButton.setOnClickListener{
            val carBrandItem = binding.carBrand.selectedItem.toString()
            val carModelItem = binding.carModel.selectedItem.toString()
            val intent = Intent(context, AdsListActivity::class.java)
            with(intent)
            {
                putExtra("car_brand_item", carBrandItem)
                putExtra("car_model_item", carModelItem)
            }
            startActivity(intent)

        }


        val spinnerBrand = getView()?.findViewById<Spinner>(R.id.car_brand)
        val spinnerModel = getView()?.findViewById<Spinner>(R.id.car_model)

        fun adapterSpinner(array: Array<String>, spinner: Spinner) {
            val adapterSpinner =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, array) }
            adapterSpinner?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner
        }

        if (spinnerBrand != null) {
            adapterSpinner(resources.getStringArray(R.array.car_brands), spinnerBrand)
        }

        binding.carBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.audi_models),
                                spinnerModel)
                        }
                    }
                    1 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.bmw_models),
                                spinnerModel)
                        }
                    }
                    2 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.toyota_models),
                                spinnerModel)
                        }
                    }
                    3 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.renault_models),
                                spinnerModel)
                        }
                    }
                    4 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.peugeot_models),
                                spinnerModel)
                        }
                    }
                    5 -> {
                        if (spinnerModel != null) {
                            adapterSpinner(resources.getStringArray(R.array.volvo_models),
                                spinnerModel)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun verifyIsUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(context, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}