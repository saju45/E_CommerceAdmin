package com.example.eadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eadmin.R
import com.example.eadmin.databinding.AlloderItemLayoutBinding
import com.example.eadmin.model.AllOderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOderAdapter(val context:Context,val list:ArrayList<AllOderModel>):RecyclerView.Adapter<AllOderAdapter.AllOderVideHolder>() {



    inner class AllOderVideHolder(view: View):RecyclerView.ViewHolder(view){

        val binding=AlloderItemLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOderVideHolder {

        return AllOderVideHolder(LayoutInflater.from(context).inflate(R.layout.alloder_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: AllOderVideHolder, position: Int) {

        holder.binding.productName.text=list[position].name
        holder.binding.productPrice.text=list[position].price

        holder.binding.cancelBtn.setOnClickListener {

            updateStatus("cancel",list[position].orderId!!)
           // holder.binding.dispachedBtn.text="cancel"
            holder.binding.dispachedBtn.visibility=GONE


        }

        when(list[position].status){
            "ordered" ->{
                holder.binding.dispachedBtn.text="dispatched"
                holder.binding.dispachedBtn.setOnClickListener {
                    updateStatus("dispatched",list[position].orderId!!)
                }
            }
            "dispatched" ->{
                holder.binding.dispachedBtn.text="delivered"

                holder.binding.dispachedBtn.setOnClickListener {
                    updateStatus("delivered",list[position].orderId!!)
                }

            }
            "delivered" ->{
                holder.binding.cancelBtn.visibility=GONE
                holder.binding.dispachedBtn.text="all ready delivered"


             /*   holder.binding.dispachedBtn.setOnClickListener {
                    updateStatus("delivered",list[position].orderId!!)
                }*/
            }
            "cancel" ->{
                holder.binding.dispachedBtn.visibility=GONE
            }

        }



    }

    fun updateStatus(str:String,doc:String){

        val hashMap= hashMapOf<String,Any>()

        hashMap["status"]=str

        Firebase.firestore.collection("allOrders")
            .document(doc)
            .update(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "status updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}