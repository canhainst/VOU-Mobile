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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import com.squareup.picasso.Picasso
import java.util.Calendar
import java.util.Date

class HorizontalEventsAdapter(private val itemList: List<Event>, private val gameViewModel: GameViewModel): RecyclerView.Adapter<HorizontalEventsAdapter.MyViewHolder>(){
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
        dialogView.findViewById<TextView>(R.id.Time).text = Helper.getTimeRangeString(itemList[position])
        dialogView.findViewById<TextView>(R.id.script2).text = itemList[position].eventName
        dialogView.findViewById<TextView>(R.id.detail).text = itemList[position].eventDetail
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Play"
        dialogView.findViewById<Button>(R.id.btnBack).text = "Back"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        //lay ngay hien tai
        val curTime = Helper.dateToString(Date())
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(itemList[position].startTime!!)!!
        calendar.add(Calendar.MINUTE, 10)
        val time2 = Helper.dateToString(calendar.time)

        if (itemList[position].typeOfEvent == 0 && Helper.isTimeAfter(curTime, itemList[position].endTime)) {
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has ended!", Toast.LENGTH_SHORT).show()
        } else if (itemList[position].typeOfEvent == 1 && Helper.isTimeAfter(curTime, time2)){
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has started!", Toast.LENGTH_SHORT).show()
        }

        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            if (itemList[position].typeOfEvent == 0  && Helper.isTimeBefore(curTime, itemList[position].startTime)){
                Toast.makeText(context, "The event has not started yet!", Toast.LENGTH_SHORT).show()
            }  else{
                EventViewModelProviderSingleton.getEventViewModel().chooseEvent(itemList[position])
                gameViewModel.setGame(itemList[position].typeOfEvent, context)
                gameViewModel.startGame()
            }
        }


        dialogBuilder.show()
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}