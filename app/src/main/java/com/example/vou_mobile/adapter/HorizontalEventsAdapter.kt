package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Event
import com.squareup.picasso.Picasso

class HorizontalEventsAdapter(private val itemList: List<Event>): RecyclerView.Adapter<HorizontalEventsAdapter.MyViewHolder>(){
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_event_layout, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val eventPicture: ImageView = itemView.findViewById(R.id.event)
        val markEvent: ImageButton = itemView.findViewById(R.id.markEvent)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        Picasso.get()
            .load(item.eventPictureUrl)
            .into(holder.eventPicture)

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
        dialogView.findViewById<TextView>(R.id.script).text = itemList[position].eventName
        Picasso.get()
            .load(itemList[position].eventPictureUrl)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text =
            when (itemList[position].typeOfEvent){
                0 -> "${itemList[position].startTime} - ${itemList[position].endTime}"
                1 -> itemList[position].startTime
                 else -> ""
            }
        dialogView.findViewById<TextView>(R.id.script2).text = itemList[position].eventName
        dialogView.findViewById<TextView>(R.id.detail).text = itemList[position].eventDetail
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Play"

        dialogBuilder.show()
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}