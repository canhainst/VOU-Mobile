package com.example.vou_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vou_mobile.adapter.GetVoucherAdapter
import com.example.vou_mobile.databinding.ActivityGetVoucherBinding
import com.example.vou_mobile.model.VoucherInEvent
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.VoucherService
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetVoucherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGetVoucherBinding
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var idEvent: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idEvent = eventViewModel.curEvent.value!!.id

        val api = RetrofitClient.instance.create(VoucherService::class.java)
        api.getVoucherByIdEvent(idEvent).enqueue(object : Callback<List<VoucherInEvent>> {
            override fun onResponse(
                call: Call<List<VoucherInEvent>>,
                response: Response<List<VoucherInEvent>>
            ) {
                if (response.isSuccessful) {
                    val vouchers = response.body()?.filter { it.total_quantity > 0 }
                    binding.rcvVoucherList.adapter = GetVoucherAdapter(vouchers!!)
                    vouchers.forEach { voucher ->
                        Log.d("Voucher", "Voucher Code: ${voucher.voucher.voucher_code}, Value: ${voucher.voucher.value}")
                        // You can update the UI or handle the vouchers as needed
                    }
                } else {
                    Log.e("Error", "Failed to fetch vouchers: ${response.message()}")
                    Toast.makeText(this@GetVoucherActivity, "Failed to fetch vouchers", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<VoucherInEvent>>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(this@GetVoucherActivity, "API call failed", Toast.LENGTH_SHORT).show()
            }
        })

        binding.rcvVoucherList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}