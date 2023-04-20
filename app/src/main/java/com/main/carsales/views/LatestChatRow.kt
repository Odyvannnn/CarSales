package com.main.carsales.views

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.data.ChatMessage
import com.main.carsales.data.User
import com.main.carsales.databinding.LatestChatsRowBinding
import com.xwray.groupie.viewbinding.BindableItem

class LatestChatsRow(private val chatMessage: ChatMessage): BindableItem<LatestChatsRowBinding>() {
    var chatPartnerUser: User? = null
    var chatAdIdItem: ChatMessage? = null
    private var db = Firebase.firestore
    override fun bind(viewBinding: LatestChatsRowBinding, position: Int) {
        viewBinding.latestChatMessage.text = chatMessage.text

        db.collection("ads").document(chatMessage.adId).get()
            .addOnSuccessListener {
                val carBrand = it.data?.get("car_brand").toString()
                val carModel = it.data?.get("car_model").toString()
                val pic1 = it.data?.get("pic1").toString()
                viewBinding.carBrand.text = carBrand
                viewBinding.carModel.text = carModel
                Glide.with(viewBinding.imageViewLatestChats)
                    .load(pic1)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                    .into(viewBinding.imageViewLatestChats)
            }

        val chatPartnerId: String = if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatMessage.toId
        } else {
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewBinding.latestChatUsername.text = chatPartnerUser?.username
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        val uid = FirebaseAuth.getInstance().uid
        val refAdId = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/latest-messages/$chatPartnerId/$uid")
        refAdId.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatAdIdItem = snapshot.getValue(ChatMessage::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun getLayout(): Int {
        return R.layout.latest_chats_row
    }

    override fun initializeViewBinding(view: View): LatestChatsRowBinding =
        LatestChatsRowBinding.bind(view)

}