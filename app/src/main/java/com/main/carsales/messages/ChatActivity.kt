package com.main.carsales.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.main.carsales.R
import com.main.carsales.data.User
import com.main.carsales.data.ChatMessage
import com.main.carsales.databinding.ActivityChatBinding
import com.main.carsales.views.ChatFromItem
import com.main.carsales.views.ChatToItem
import com.xwray.groupie.GroupieAdapter

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    val adapter = GroupieAdapter()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val uid = FirebaseAuth.getInstance().uid  ?: ""
        toUser = intent.getParcelableExtra(uid)
        supportActionBar?.title = toUser?.username

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewChatLog.adapter = adapter
        binding.recyclerViewChatLog.layoutManager = LinearLayoutManager(this)

        listenForMessages()

        binding.sendMessageButton.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null){
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        toUser?.let { ChatToItem(chatMessage.text, it) }?.let { adapter.add(it) }
                    }
                }
                binding.recyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun performSendMessage(){
        val text = binding.writeMessageText.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid ?: ""
        val reference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/user-messages/$fromId/$toId").push()
        if (fromId == null) return

        val toReference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId,
            toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                binding.writeMessageText.text.clear()
                binding.recyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        val latestChatsReference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/latest-messages/$fromId/$toId")
        latestChatsReference.setValue(chatMessage)
        val latestChatsToReference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/latest-messages/$toId/$fromId")
        latestChatsToReference.setValue(chatMessage)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
