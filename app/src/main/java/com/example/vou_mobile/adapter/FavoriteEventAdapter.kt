package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.ShakingGameActivity
import com.example.vou_mobile.databinding.DetailDialogBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.viewModel.GameViewModel
import com.example.vou_mobile.viewModel.MainViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FavoriteEventAdapter(private var events: List<Event>, private val viewModel: MainViewModel, private val gameViewModel: GameViewModel) : RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        Picasso.get()
            .load(event.eventPictureUrl)
            .into(holder.imageView)

        holder.time.text = Helper.getTimeRangeString(event)
        holder.notifButton.setOnClickListener {
            showConfirmDialog(event)
        }
        holder.itemView.setOnClickListener {
            showEventDialog(event)
        }
    }

    private fun showConfirmDialog(event: Event) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Confirm")
            .setMessage("Are you sure you want to remove this event from your favorites?")
            .setPositiveButton("Remove") { dialog, _ ->
                viewModel.removeFavoriteEvent(event)
                dialog.dismiss()
            }
            .setNegativeButton("Back") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.setOnShowListener {
            val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            negativeButton.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
        }
        alertDialog.show()
    }

    private fun showEventDialog(event: Event) {
        val binding = DetailDialogBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.brandName.text = event.brandName
        binding.script.text = event.eventName
        binding.script2.text = event.eventName
        binding.detail.text = event.eventDetail
        Picasso.get()
            .load(event.eventPictureUrl)
            .into(binding.picture)
        binding.Time.text = Helper.getTimeRangeString(event)

        binding.btnBack.setOnClickListener {
            dialogBuilder.dismiss()
        }

        //lay ngay hien tai
        val curTime = Helper.dateToString(Date())
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(event.startTime!!)!!
        calendar.add(Calendar.MINUTE, 10)
        val time2 = Helper.dateToString(calendar.time)

        if (event.typeOfEvent == 0 && Helper.isTimeAfter(curTime, event.endTime)) {
            binding.btnDirection.visibility = View.GONE
            Toast.makeText(context, "The event has ended!", Toast.LENGTH_SHORT).show()
        } else if (event.typeOfEvent == 1 && !Helper.isTimeInRange(curTime, event.startTime, time2)){
            binding.btnDirection.visibility = View.GONE
            Toast.makeText(context, "The event has started!", Toast.LENGTH_SHORT).show()
        }

        binding.btnDirection.setOnClickListener {
            if (Helper.isTimeBefore(curTime, event.startTime)){
                Toast.makeText(context, "The event has not started yet!", Toast.LENGTH_SHORT).show()
            }  else{
                gameViewModel.setGame(event.typeOfEvent, context)
                gameViewModel.startGame()
            }
        }

        dialogBuilder.show()
    }
}
