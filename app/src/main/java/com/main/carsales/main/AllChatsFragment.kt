package com.main.carsales.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.main.carsales.data.ChatMessage
import com.main.carsales.databinding.FragmentAllChatsBinding
import com.main.carsales.messages.ChatActivity
import com.main.carsales.views.LatestChatsRow
import com.xwray.groupie.GroupieAdapter


class AllChatsFragment : Fragment()  {
    private var _binding: FragmentAllChatsBinding? = null
    private val binding: FragmentAllChatsBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().uid  ?: ""
        binding.latestChatsRecyclerView.adapter = adapter
        binding.latestChatsRecyclerView.layoutManager = LinearLayoutManager(context)
        listenForLatestChats()
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(context, ChatActivity::class.java)
            val row = item as LatestChatsRow
            val adId = row.chatAdIdItem?.adId
            intent.putExtra(uid, row.chatPartnerUser)
            intent.putExtra("adId", adId)
            startActivity(intent)
        }
    }

    val latestCharsMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewChats() {
        adapter.clear()
        latestCharsMap.values.forEach{
            adapter.add(LatestChatsRow(it))
        }
    }

    private fun listenForLatestChats() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://car-sales-33dcf-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("/latest-messages/$fromId")
        Log.d(TAG, ref.toString())
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestCharsMap[snapshot.key!!] = chatMessage
                adapter.add(LatestChatsRow(chatMessage))
                refreshRecyclerViewChats()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestCharsMap[snapshot.key!!] = chatMessage
                adapter.add(LatestChatsRow(chatMessage))
                refreshRecyclerViewChats()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    val adapter = GroupieAdapter()

}
