package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Voucher

class VerticalUsedVoucherAdapter (private val itemList: List<Voucher>) : RecyclerView.Adapter<VerticalUsedVoucherAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgVoucherType: ImageView = itemView.findViewById(R.id.imgVoucherType)
        val brandName: TextView = itemView.findViewById(R.id.brand_name)
        val voucherScript: TextView = itemView.findViewById(R.id.voucherScript)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDate)
        val imgBrand: ImageView = itemView.findViewById(R.id.imgBrand)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_my_voucher_item_layout, parent, false)
        return MyViewHolder(view)
    }
    override fun getItemCount() = itemList.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.brandName.text = "Brand Name"
        holder.voucherScript.text = itemList[position].description
        holder.expiryDate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        holder.expiryDate.text = "Used"

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
        }
    }
}