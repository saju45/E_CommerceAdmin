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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eadmin.R
import com.example.eadmin.databinding.FragmentSliderBinding
import com.example.eadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class SliderFragment : Fragment() {

    private lateinit var binding:FragmentSliderBinding
    private var imageUrl:Uri?=null
    private lateinit var dialog:Dialog

    private var lunchGalleryActivity=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode==Activity.RESULT_OK){

            imageUrl=it.data!!.data
            binding.simpleImage.setImageURI(imageUrl)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSliderBinding.inflate(layoutInflater)


        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)




        binding.apply {

            simpleImage.setOnClickListener {

                val intent=Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                lunchGalleryActivity.launch(intent)
            }

            btnUpload.setOnClickListener {

                if(imageUrl!=null){

                    UploadImage(imageUrl!!)

                }else{
                    Toast.makeText(requireContext(),"please select image",Toast.LENGTH_SHORT).show()
                }

            }
        }

        return binding.root
    }


    fun UploadImage( imageUri:Uri){

        dialog.show()

        val fileName=UUID.randomUUID().toString()+".jpg"

        val refStorage=FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refStorage.putFile(imageUri)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->

                    storageData(image.toString())
                    dialog.dismiss()


                }

            }.addOnFailureListener {

                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storageData(image: String) {

        val db=Firebase.firestore

        val data= hashMapOf<String,Any>(
            "img" to image
        )

        db.collection("slider").document("item").set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "slider uploaded", Toast.LENGTH_SHORT).show()
           // binding.simpleImage.setImageResource(R.drawable.gallary)
        }.addOnFailureListener{
            dialog.dismiss()
            Toast.makeText(requireContext(), "something is wrong", Toast.LENGTH_SHORT).show()

        }

    }

}