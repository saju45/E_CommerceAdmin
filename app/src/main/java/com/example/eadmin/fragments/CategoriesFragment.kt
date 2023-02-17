package com.example.eadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eadmin.R
import com.example.eadmin.adapters.CategoryAdapter
import com.example.eadmin.databinding.FragmentCategoriesBinding
import com.example.eadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CategoriesFragment : Fragment() {

    private lateinit var binding:FragmentCategoriesBinding

    private var imageUrl: Uri?=null
    private lateinit var dialog: Dialog

    private var lunchGalleryActivity=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode== Activity.RESULT_OK){

            imageUrl=it.data!!.data
            binding.simpleImage.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCategoriesBinding.inflate(layoutInflater)

        getData()

        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)


        binding.apply {

            simpleImage.setOnClickListener {
                val intent= Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                lunchGalleryActivity.launch(intent)
            }

            btnUpload.setOnClickListener {
                validateData(binding.categoryName.text.toString())
            }
        }

        return binding.root
    }

    private fun validateData(name:String) {


        if (name.isEmpty()){

            binding.categoryName.error="please provide category name"
            binding.categoryName.requestFocus()
        }else if (imageUrl==null){

            Toast.makeText(requireContext(), "Please choose any image", Toast.LENGTH_SHORT).show()
        }else{
            UploadeImage(name)
        }

    }

    private fun UploadeImage(categoryName:String) {

        dialog.show()

        val fileName= UUID.randomUUID().toString()+".jpg"

        val refStorage= FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->

                    storageData(image.toString(),categoryName)
                    dialog.dismiss()

                }

            }.addOnFailureListener {

                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong with storage", Toast.LENGTH_SHORT).show()
            }

    }

    private fun storageData(image: String,categoryName: String) {

        val db= Firebase.firestore

        val data= hashMapOf<String,Any>(
            "img" to image,
            "cate" to categoryName
        )
        db.collection("category").add(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "category added", Toast.LENGTH_SHORT).show()
            binding.simpleImage.setImageURI(null)
            binding.simpleImage.setImageDrawable(resources.getDrawable(R.drawable.gallary))
            binding.categoryName.text=null
            getData()
            // binding.simpleImage.setImageResource(R.drawable.gallary)
        }.addOnFailureListener{
            dialog.dismiss()
            Toast.makeText(requireContext(), "something is wrong", Toast.LENGTH_SHORT).show()

        }

    }

    private fun getData() {

        val list=ArrayList<CategoryModel>()
        Firebase.firestore.collection("category")
            .get()
            .addOnSuccessListener {

                list.clear()
                for (doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }

                binding.categoriesRv.adapter=CategoryAdapter(requireContext(),list)


            }.addOnFailureListener {

            }
    }

}