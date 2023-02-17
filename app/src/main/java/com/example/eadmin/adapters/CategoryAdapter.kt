package com.example.eadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eadmin.R
import com.example.eadmin.databinding.ItemCategoryLayoutBinding
import com.example.eadmin.model.CategoryModel

class CategoryAdapter(val context:Context,val list: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view:View): RecyclerView.ViewHolder(view) {

        val binding=ItemCategoryLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view=LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {


        holder.binding.categoryName.text=list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.ctImage)
    }

    override fun getItemCount(): Int {

        return list.size
    }
}