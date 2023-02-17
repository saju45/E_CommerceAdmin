package com.example.eadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.eadmin.databinding.ActivityMainBinding
import com.example.eadmin.fragments.AllOrderActivity
import com.example.eadmin.fragments.CategoriesFragment
import com.example.eadmin.fragments.ProductFragment
import com.example.eadmin.fragments.SliderFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addToCategories.setOnClickListener {

            replace(CategoriesFragment())
            binding.layout1.visibility=ConstraintLayout.GONE
          Toast.makeText(this,"Categories",Toast.LENGTH_SHORT).show()
        }


        binding.addProduct.setOnClickListener {
            replace(ProductFragment())
            binding.layout1.visibility=ConstraintLayout.GONE
            Toast.makeText(this,"Product",Toast.LENGTH_SHORT).show()

        }

        binding.addSlider.setOnClickListener {
            replace(SliderFragment())
            binding.layout1.visibility=ConstraintLayout.GONE
            Toast.makeText(this,"Slider",Toast.LENGTH_SHORT).show()

        }

        binding.alOrder.setOnClickListener {
            binding.layout1.visibility=ConstraintLayout.GONE
            startActivity(Intent(this,AllOrderActivity::class.java))
        }
    }

    fun replace(fragment:Fragment){

        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
    }
}