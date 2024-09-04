package com.example.vou_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vou_mobile.adapter.GetVoucherAdapter
import com.example.vou_mobile.databinding.ActivityGetVoucherBinding
import com.example.vou_mobile.model.Voucher

class GetVoucherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGetVoucherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listVoucher = listOf(
            Voucher("1", "1", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", 90, 100000, null, "Activate", "ONLINE", ""),
            Voucher("2", "2", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", 88, 50000, null, "Activate", "OFFLINE", ""))
        binding.rcvVoucherList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.rcvVoucherList.adapter = GetVoucherAdapter(listVoucher)

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}