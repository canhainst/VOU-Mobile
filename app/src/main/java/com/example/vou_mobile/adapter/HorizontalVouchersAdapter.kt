package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.model.VoucherUser
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.VoucherService
import com.example.vou_mobile.services.addVoucherRequest
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HorizontalVouchersAdapter(private val itemList: List<Voucher>) : RecyclerView.Adapter<HorizontalVouchersAdapter.MyViewHolder>(){
    private var listener: OnItemClickListener? = null
    private val brandService = RetrofitClient.instance.create(BrandService::class.java)
    private lateinit var sharedPreferences: SharedPreferences

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val voucherPictureUrl: ImageView = itemView.findViewById(R.id.voucherPicture)
        val brandName: TextView = itemView.findViewById(R.id.brand_name)
        val description: TextView = itemView.findViewById(R.id.voucher_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_vouchers_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        Picasso.get()
            .load(item.image)
            .into(holder.voucherPictureUrl)

        val callBrand = brandService.getBrandByUuid(item.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    holder.brandName.text = brand!!.brand_name
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        holder.description.text = item.description

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
            showCustomDialog(holder.itemView.context, position)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showCustomDialog(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val callBrand = brandService.getBrandByUuid(itemList[position].id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    dialogView.findViewById<TextView>(R.id.brand_name).text = brand!!.brand_name
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })
        dialogView.findViewById<TextView>(R.id.script).text = "Voucher Script"
        Picasso.get()
            .load(itemList[position].image)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = "Expiration: ${"exp"}"
        dialogView.findViewById<TextView>(R.id.script2).text = "Voucher Script"
        dialogView.findViewById<TextView>(R.id.detail).text = itemList[position].description
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Use"
        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {

            dialogBuilder.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnBack).text = "Back"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }
}