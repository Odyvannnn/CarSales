package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.main.carsales.R
import com.main.carsales.ad.AdActivity
import com.main.carsales.databinding.FragmentAccountBinding
import com.main.carsales.databinding.FragmentAddAdBinding

class AddAdFragment : Fragment() {

    private var _binding: FragmentAddAdBinding? = null
    private val binding: FragmentAddAdBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addAdButton.setOnClickListener{
            startActivity(Intent(context, AdActivity::class.java))
        }
    }
}