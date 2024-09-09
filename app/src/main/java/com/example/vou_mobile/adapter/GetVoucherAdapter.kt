package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.DialogGetTurnSuccessBinding
import com.example.vou_mobile.databinding.DialogGetVoucherSuccessBinding
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.model.VoucherInEvent
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.ItemService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.VoucherService
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class GetVoucherAdapter (private var vouchers: List<VoucherInEvent>) : RecyclerView.Adapter<GetVoucherAdapter.ViewHolder>(){
    private lateinit var context: Context
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        val voucherImg: ImageView = itemView.findViewById(R.id.voucherImg)
        val brandName: TextView = itemView.findViewById(R.id.brandName)
        val script: TextView = itemView.findViewById(R.id.script)
        val itemList: TextView = itemView.findViewById(R.id.itemList)
        val btnGet: Button = itemView.findViewById(R.id.btnGet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.get_voucher_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return vouchers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = vouchers[position]
        //
        Picasso.get()
            .load(voucher.voucher.image)
            .into(holder.voucherImg)

        val api = RetrofitClient.instance.create(BrandService::class.java)
        api.getBrandByUuid(voucher.voucher.id_brand).enqueue(object : Callback<Brand> {
            override fun onResponse(
                call: Call<Brand>,
                response: Response<Brand>
            ) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    holder.brandName.text = brand.toString()
                } else {
                    Log.e("Error", "Failed to fetch vouchers: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
            }
        })

        RetrofitClient.instance.create(ItemService::class.java)
            .getItemsByIdEvent(eventViewModel.curEvent.value!!.id).enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()!!
                    val itemNames = items.map { it.name } // Extract the names from the items list
                    val formattedText = itemNames.joinToString(", ")
                    holder.itemList.text = formattedText
                } else {
                   Log.d("Err get items","Failed to fetch items")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("YourActivity", "Error fetching items: ${t.message}")
            }
        })

        holder.btnGet.setOnClickListener {

            if (holder.btnGet.text == "Get"){
                holder.btnGet.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_grey)
                showSuccessDialog(voucher.voucher)
                holder.btnGet.text = "Received"
            } else{
                holder.btnGet.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_purple)
                holder.btnGet.text = "Get"
            }
        }
        holder.script.text = voucher.voucher.description
    }

    @SuppressLint("SetTextI18n")
    private fun showSuccessDialog(voucher: Voucher) {
        val binding = DialogGetVoucherSuccessBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.textWithBrandName.text = "You have received 1 voucher from ${"Brand name"}"
        binding.script.text = voucher.description
        binding.animationVoucher.playAnimation()
        dialogBuilder.show()
    }
}