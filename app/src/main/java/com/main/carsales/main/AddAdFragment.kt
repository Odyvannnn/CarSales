package com.main.carsales.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.main.carsales.ad.AddAdActivity
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
        (activity as AppCompatActivity).supportActionBar?.title = "Добавить объявление"

        binding.addAdButton.setOnClickListener{
            startActivity(Intent(context, AddAdActivity::class.java))
        }
    }
}