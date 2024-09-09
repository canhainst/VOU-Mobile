package com.example.vou_mobile.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityRequestTurnAtivityBinding
import com.example.vou_mobile.databinding.DialogGetTurnSuccessBinding
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModelProviderSingleton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestTurnAtivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestTurnAtivityBinding
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private val gameViewModel = GameViewModelProviderSingleton.getGameViewModel()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private lateinit var eventId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestTurnAtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("uuid", null)!!
        eventId = eventViewModel.curEvent.value!!.id

        //brand name
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(eventViewModel.curEvent.value!!.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        binding.tvBrandName.text = brand.brand_name
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        //event name
        binding.tvEventName.text = eventViewModel.curEvent.value!!.name

        //playThrough
        gameViewModel.loadPlaythrough(eventId, userId)
        binding.tvPlaythrough.text = gameViewModel.playthrough.value.toString()
        gameViewModel.playthrough.observe(this, Observer {
            binding.tvPlaythrough.text = it.toString()
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRequest.setOnClickListener {
//            showSuccessDialog()
        }

        binding.btnShareFb.setOnClickListener {

        }
    }

    private fun showSuccessDialog() {
        val binding = DialogGetTurnSuccessBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        binding.animationHeart.playAnimation()
        dialogBuilder.show()
    }
}