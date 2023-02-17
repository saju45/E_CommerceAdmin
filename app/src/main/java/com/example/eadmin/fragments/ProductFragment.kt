package com.example.eadmin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.example.eadmin.AddProductActivity
import com.example.eadmin.R
import com.example.eadmin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {

    private lateinit var binding:FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(),AddProductActivity::class.java))
        }

        return binding.root
    }

}