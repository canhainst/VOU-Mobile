package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Voucher
import com.squareup.picasso.Picasso

class HorizontalVouchersAdapter(private val itemList: List<Voucher>) : RecyclerView.Adapter<HorizontalVouchersAdapter.MyViewHolder>(){
    private var listener: OnItemClickListener? = null

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
            .load(item.voucherPictureUrl)
            .into(holder.voucherPictureUrl)

        holder.brandName.text = item.brandName
        holder.description.text = item.voucherDetail

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

        dialogView.findViewById<TextView>(R.id.brand_name).text = itemList[position].brandName
        dialogView.findViewById<TextView>(R.id.script).text = itemList[position].script
        Picasso.get()
            .load(itemList[position].voucherPictureUrl)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = "Expiration: ${itemList[position].expiration}"
        dialogView.findViewById<TextView>(R.id.script2).text = itemList[position].script
        dialogView.findViewById<TextView>(R.id.detail).text = itemList[position].voucherDetail
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Get"
        dialogView.findViewById<Button>(R.id.btnBack).text = "Back"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }
}