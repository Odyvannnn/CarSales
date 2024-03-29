package com.main.carsales.main

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.ad.AdActivity
import com.main.carsales.adapters.AdAdapter
import com.main.carsales.auth.AuthActivity
import com.main.carsales.data.Ad
import com.main.carsales.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adList: ArrayList<Ad>
    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Мои объявления"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.account_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.exit -> {
                        val intent = Intent(context, AuthActivity::class.java)
                        FirebaseAuth.getInstance().signOut()
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                        (activity as AppCompatActivity).finish()
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        adList = arrayListOf()
        val adAdapter = AdAdapter(adList)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        db.collection("ads").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val ad: Ad? = data.toObject(Ad::class.java)
                        if (ad != null) {
                            if (ad.seller_uid == auth.uid) {
                                adList.add(ad)
                                binding.noAdsText.text = ""
                            }
                        }
                    }
                    recyclerView.adapter = adAdapter
                    adAdapter.setOnItemClickListener(object : AdAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AdActivity::class.java)
                            with(intent) {
                                putExtra("car_brand", adList[position].car_brand)
                                putExtra("car_model", adList[position].car_model)
                                putExtra("price", adList[position].price)
                                putExtra("year", adList[position].year)
                                putExtra("mileage", adList[position].mileage)
                                putExtra("car_drive_type", adList[position].car_drive_type)
                                putExtra("engine_power", adList[position].engine_power)
                                putExtra("engine_type", adList[position].engine_type)
                                putExtra("transmission", adList[position].transmission)
                                putExtra("pic1", adList[position].pic1)
                                putExtra("pic2", adList[position].pic2)
                                putExtra("pic3", adList[position].pic3)
                                putExtra("pic4", adList[position].pic4)
                                putExtra("pic5", adList[position].pic5)
                                putExtra("pic6", adList[position].pic6)
                                putExtra("pic7", adList[position].pic7)
                                putExtra("pic8", adList[position].pic8)
                                putExtra("pic9", adList[position].pic9)
                                putExtra("seller_uid", adList[position].seller_uid)
                                putExtra("info_ad", adList[position].info_ad)
                                putExtra("city", adList[position].city)
                                putExtra("adId", adList[position].adId)
                                putExtra("status", adList[position].status)
                            }
                            startActivity(intent)
                        }
                    })
                }
            }
            .addOnFailureListener {
            }
    }

}