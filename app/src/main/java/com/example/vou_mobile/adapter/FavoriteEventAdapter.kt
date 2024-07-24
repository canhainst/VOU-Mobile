package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Event
import com.squareup.picasso.Picasso

class FavoriteEventAdapter (private var events: List<Event>): RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {
    private lateinit var context: Context
    class ViewHolder (private val itemView: View) : RecyclerView.ViewHolder(itemView){
        val brand: TextView = itemView.findViewById(R.id.brand)
        val notifButton: ImageButton = itemView.findViewById(R.id.notifButton)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.brand.text = event.brandName
        holder.notifButton.setOnClickListener {
            showConfirmDialog(event)
        }
        Picasso.get()
            .load(event.eventPictureUrl)
            .into(holder.imageView)

        holder.time.text = "${event.startTime} - ${event.endTime}"
    }

    private fun showConfirmDialog(event: Event) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Confirm")
            .setMessage("Are you sure you want to remove this event from your favorites?")
            .setPositiveButton("Confirm") { dialog, which ->
                // Xử lý khi người dùng chọn "Có"

            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Xử lý khi người dùng chọn "Không"
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}