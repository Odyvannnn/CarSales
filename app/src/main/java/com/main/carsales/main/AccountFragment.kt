package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.main.carsales.auth.AuthActivity
import com.main.carsales.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
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

        binding.signoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, AuthActivity::class.java))
        }
    }

}