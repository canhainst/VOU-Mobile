package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerticalUnusedVoucherAdapter(private val itemList: List<Voucher>) : RecyclerView.Adapter<VerticalUnusedVoucherAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val brandName: TextView = itemView.findViewById(R.id.brand_name)
        val voucherScript: TextView = itemView.findViewById(R.id.voucherScript)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDate)
        val imgBrand: ImageView = itemView.findViewById(R.id.imgBrand)
        var quantity: TextView = itemView.findViewById(R.id.quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_my_voucher_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(itemList[position].id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        holder.brandName.text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(holder.imgBrand)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })
        holder.voucherScript.text = itemList[position].description
        holder.expiryDate.text = "Expiry Date: ${"itemList[position].expiration"}"
        holder.quantity.text = "x ${itemList[position].quantity}"

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
        }
    }
}