package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
                            intent.putExtra("pic1", adList[position].pic1)
                            intent.putExtra("pic2", adList[position].pic2)
                            intent.putExtra("pic3", adList[position].pic3)
                            intent.putExtra("pic4", adList[position].pic4)
                            intent.putExtra("pic5", adList[position].pic5)
                            intent.putExtra("pic6", adList[position].pic6)
                            intent.putExtra("pic7", adList[position].pic7)
                            intent.putExtra("pic8", adList[position].pic8)
                            intent.putExtra("pic9", adList[position].pic9)
                            startActivity(intent)
                        }
                    })
                }
            }
            .addOnFailureListener {
            }
    }
}