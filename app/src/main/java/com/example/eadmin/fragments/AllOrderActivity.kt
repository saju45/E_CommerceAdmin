package com.example.eadmin.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eadmin.R
import com.example.eadmin.adapters.AllOderAdapter
import com.example.eadmin.databinding.ActivityAllOrderBinding
import com.example.eadmin.model.AllOderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAllOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getAllOder()

    }

    private fun getAllOder() {

        val list=ArrayList<AllOderModel>()

        Firebase.firestore.collection("allOrders")
            .get()
            .addOnSuccessListener {

                list.clear()

                for (data in it){

                    val mainData=data.toObject(AllOderModel::class.java)
                    list.add(mainData)
                }
                binding.recyclerView.adapter=AllOderAdapter(this,list)


            }.addOnFailureListener {

            }
    }
}