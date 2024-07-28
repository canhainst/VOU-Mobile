package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Voucher
class VerticalUnusedVoucherAdapter(private val itemList: List<Voucher>) : RecyclerView.Adapter<VerticalUnusedVoucherAdapter.MyViewHolder>() {
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
        holder.brandName.text = itemList[position].brandName
        holder.voucherScript.text = itemList[position].script
        holder.expiryDate.text = "Expiry Date: ${itemList[position].expiration}"

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
        }
    }
}