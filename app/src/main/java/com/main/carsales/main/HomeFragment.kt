package com.main.carsales.main

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
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = FirebaseFirestore.getInstance()
        db.collection("ads").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val ad: Ad? = data.toObject(Ad::class.java)
                        if (ad != null) {
                            if (ad.status == "ready"){
                                adList.add(ad)
                            }
                        }
                    }
                    recyclerView.adapter = AdAdapter(adList)
                }
            }
            .addOnFailureListener {
            }
    }
}