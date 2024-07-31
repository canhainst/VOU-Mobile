package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.DialogGetTurnSuccessBinding
import com.example.vou_mobile.databinding.DialogGetVoucherSuccessBinding
import com.example.vou_mobile.model.Voucher
import com.squareup.picasso.Picasso

class GetVoucherAdapter (private var vouchers: List<Voucher>) : RecyclerView.Adapter<GetVoucherAdapter.ViewHolder>(){
    private lateinit var context: Context
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
        //brand = getBrandFromID(voucher.brandId)
        Picasso.get()
            .load(voucher.voucherPictureUrl)
            .into(holder.voucherImg)

        holder.brandName.text = voucher.brandName
        holder.script.text = voucher.script
        holder.itemList.text = "1 Ngọc xanh, 2 Ngọc đỏ"
        holder.btnGet.setOnClickListener {
            if (holder.btnGet.text == "Get"){
                holder.btnGet.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_grey)
                showSuccessDialog(voucher)
                holder.btnGet.text = "Received"
            } else{
                holder.btnGet.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_purple)
                holder.btnGet.text = "Get"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSuccessDialog(voucher: Voucher) {
        val binding = DialogGetVoucherSuccessBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.textWithBrandName.text = "You have received 1 voucher from ${voucher.brandName}"
        binding.script.text = voucher.script
        binding.animationVoucher.playAnimation()
        dialogBuilder.show()
    }
}