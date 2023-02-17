package com.example.eadmin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eadmin.adapters.AddProductImageAdapter
import com.example.eadmin.databinding.ActivityAddProductBinding
import com.example.eadmin.model.AddProductModel
import com.example.eadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddProductBinding
    private lateinit var list:ArrayList<Uri>
    private lateinit var listImages:ArrayList<String>
    private lateinit var adapter:AddProductImageAdapter
    private var coverImage:Uri?=null
    private lateinit var dialog: Dialog
    private var coverImageUri:String?=""

    private lateinit var categoryList:ArrayList<String>
    private var lunchGalleryActivity=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode== Activity.RESULT_OK){

            coverImage=it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
            binding.productCoverImg.visibility= View.VISIBLE
        }
    }

    private var lunchGalleryActivity2=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode== Activity.RESULT_OK){

         val imageUrl =it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
            //binding.productCoverImg.setImageURI(imageUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProductCategory()
        list= ArrayList()
        listImages= ArrayList()

        dialog= Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)


        binding.selectCoverImg.setOnClickListener {
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            lunchGalleryActivity.launch(intent)

        }

        binding.productImgBtn.setOnClickListener {
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            lunchGalleryActivity2.launch(intent)

        }

        adapter= AddProductImageAdapter(this,list)
        binding.productImageRv.adapter=adapter


        binding.submitBtn.setOnClickListener {
            ValidateData()
        }

    }

    private fun ValidateData() {

        if (binding.productNameEt.text.toString().isEmpty())
        {
            binding.productNameEt.error="please enter product name"
            binding.productNameEt.requestFocus()
        }else if (binding.productDescription.text.toString().isEmpty()){

            binding.productDescription.error="Empty"
            binding.productDescription.requestFocus()
        }else if (binding.productMRP.text.toString().isEmpty()){
            binding.productMRP.error="Empty"
            binding.productMRP.requestFocus()

        }else if (binding.productSp.text.toString().isEmpty()){
            binding.productSp.error="Empty"
            binding.productSp.requestFocus()
        }else if(coverImage==null){

            Toast.makeText(this, "Please choose cover image", Toast.LENGTH_SHORT).show()
        }else if (list.size<1){

            Toast.makeText(this, "please select product images", Toast.LENGTH_SHORT).show()
        }else{
            UploadImage()
        }

    }

    private fun UploadImage() {

        dialog.show()

        val fileName= UUID.randomUUID().toString()+".jpg"

        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->

                    coverImageUri=image.toString()
                    UploadProductImage()

                }

            }.addOnFailureListener {

                dialog.dismiss()
                Toast.makeText(this, "something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private var i=0
    private fun UploadProductImage() {

        dialog.show()

        val fileName= UUID.randomUUID().toString()+".jpg"

        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i]!!)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->

                    listImages.add(image!!.toString())
                    if (list.size==listImages.size){

                        StoreData()
                    }else{
                        i+=1
                        UploadProductImage()
                    }

                }

            }.addOnFailureListener {

                dialog.dismiss()
                Toast.makeText(this, "something went wrong with storage", Toast.LENGTH_SHORT).show()
            }


    }

    private fun StoreData() {

        val db=Firebase.firestore.collection("product")
        val key=db.document().id
        val data=AddProductModel(
            binding.productNameEt.text.toString(),
        binding.productDescription.text.toString(),
        coverImageUri.toString(),
        categoryList[binding.productCategoryDropdown.selectedItemPosition],
        binding.productMRP.text.toString(),
        key,
        binding.productSp.text.toString(),
        listImages)

        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(this, "product added", Toast.LENGTH_SHORT).show()
            binding.productNameEt.text=null
            binding.productDescription.text=null
            binding.productMRP.text=null
            binding.productSp.text=null
        }.addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setProductCategory(){
        categoryList= ArrayList()

        Firebase.firestore.collection("category")
            .get()
            .addOnSuccessListener {

                for (doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    categoryList.add(data!!.cate!!)

                }

                categoryList.add(0,"select category")

                val arrayAdapter=ArrayAdapter(this,R.layout.drop_down_item_layout,categoryList)

                binding.productCategoryDropdown.adapter=arrayAdapter
            }
    }
}