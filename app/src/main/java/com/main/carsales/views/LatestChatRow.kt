package com.main.carsales.views

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.main.carsales.R
import com.main.carsales.data.ChatMessage
import com.main.carsales.data.User
import com.main.carsales.databinding.LatestChatsRowBinding
import com.xwray.groupie.viewbinding.BindableItem

class LatestChatsRow(private val chatMessage: ChatMessage): BindableItem<LatestChatsRowBinding>() {
    var chatPartnerUser: User? = null
    override fun bind(viewBinding: LatestChatsRowBinding, position: Int) {
        viewBinding.latestChatMessage.text = chatMessage.text

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

    }

    override fun getLayout(): Int {
        return R.layout.latest_chats_row
    }

    override fun initializeViewBinding(view: View): LatestChatsRowBinding =
        LatestChatsRowBinding.bind(view)

}