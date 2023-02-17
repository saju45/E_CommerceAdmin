package com.example.eadmin.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eadmin.R
import com.example.eadmin.databinding.ImageItemBinding

class AddProductImageAdapter(val context:Context,val list: ArrayList<Uri>):
    RecyclerView.Adapter<AddProductImageAdapter.AddProductImageViewHolder>() {

    inner class AddProductImageViewHolder(view:View): RecyclerView.ViewHolder(view){
        val binding=ImageItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductImageViewHolder {

        return AddProductImageViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item,parent,false))
    }

    override fun onBindViewHolder(holder: AddProductImageViewHolder, position: Int) {

        Glide.with(context).load(list[position]).into(holder.binding.itemImage)
    }

    override fun getItemCount(): Int {

        return list.size
    }
}