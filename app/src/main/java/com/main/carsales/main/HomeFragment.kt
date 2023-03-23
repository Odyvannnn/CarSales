package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.ad.AdActivity
import com.main.carsales.adapters.AdAdapter
import com.main.carsales.data.Ad

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adList: ArrayList<Ad>
    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adList = arrayListOf()
        val adAdapter = AdAdapter(adList)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = FirebaseFirestore.getInstance()
        db.collection("ads").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val ad: Ad? = data.toObject(Ad::class.java)
                        if (ad != null) {
                            if (ad.status == "published"){
                                adList.add(ad)
                            }
                        }
                    }
                    recyclerView.adapter = adAdapter
                    adAdapter.setOnItemClickListener(object : AdAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AdActivity::class.java)
                            intent.putExtra("car_brand", adList[position].car_brand)
                            intent.putExtra("car_model", adList[position].car_model)
                            intent.putExtra("price", adList[position].price)
                            intent.putExtra("year", adList[position].year)
                            intent.putExtra("mileage", adList[position].mileage)
                            intent.putExtra("car_drive_type", adList[position].car_drive_type)
                            intent.putExtra("engine_power", adList[position].engine_power)
                            intent.putExtra("transmission", adList[position].transmission)
                            startActivity(intent)
                        }
                    })
                }
            }
            .addOnFailureListener {
            }
    }
}