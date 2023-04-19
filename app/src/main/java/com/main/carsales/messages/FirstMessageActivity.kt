package com.main.carsales.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.main.carsales.R
import com.main.carsales.data.ChatMessage
import com.main.carsales.data.User
import com.main.carsales.databinding.ActivityChatBinding
import com.main.carsales.databinding.ActivityFirstMessageBinding

class FirstMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstMessageBinding
    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_message)
        binding = ActivityFirstMessageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.sendMessageButton.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage(){
        val text = binding.writeMessageText.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = intent.getStringExtra("seller_uid")
        val reference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/user-messages/$fromId/$toId").push()
        if (fromId == null) return

        val toReference = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = toId?.let {
            ChatMessage(
                reference.key!!, text, fromId,
                it, System.currentTimeMillis() / 1000
            )
        }
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                binding.writeMessageText.text.clear()
                finish()
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