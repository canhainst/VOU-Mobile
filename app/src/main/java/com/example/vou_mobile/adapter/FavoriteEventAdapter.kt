package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.DetailDialogBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.viewModel.GameViewModel
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class FavoriteEventAdapter(private var events: List<Event>, private val gameViewModel: GameViewModel) : RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {
    private lateinit var context: Context
    private val viewModel =  EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var uuid: String

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", "")!!
        val event = events[position]

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(event.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        holder.brand.text = brand.brand_name
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })
        Picasso.get()
            .load(event.image)
            .into(holder.imageView)

        holder.notifButton.setImageResource(R.drawable.baseline_notifications_active_24)
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
                viewModel.removeFavoriteEvent(context, event, uuid){
                    dialog.dismiss()
                    Toast.makeText(context, "Event marking has been removed", Toast.LENGTH_SHORT).show()
                }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEventDialog(event: Event) {
        val binding = DetailDialogBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(event.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        binding.brandName.text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(binding.brandAvt)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })
        binding.script.text = event.name
        binding.script2.text = event.name
        Picasso.get()
            .load(event.image)
            .into(binding.picture)


        binding.Time.text = Helper.getTimeRangeString(event)

        binding.btnBack.setOnClickListener {
            dialogBuilder.dismiss()
        }

        val eventUpdate = Helper.fixEventTime(event)
        //lay ngay hien tai
        val curTime = Helper.dateToString(Date())

        //lay thoi diem cach thoi gian bat dau Quiz 20p
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(eventUpdate.start_time!!)!!
        calendar.add(Calendar.MINUTE, 20)
        val time2 = Helper.dateToString(calendar.time) //thoi gian sau 10p ke tu khi bat dau

        if (event.type?.lowercase() == "lắc xì" && Helper.isTimeAfter(curTime, event.end_time)) {
            binding.btnDirection.visibility = View.GONE
            Toast.makeText(context, "The event has ended!", Toast.LENGTH_SHORT).show()
        } else if (event.type?.lowercase() == "quiz" && Helper.isTimeAfter(curTime, time2)){
            binding.btnDirection.visibility = View.GONE
            Toast.makeText(context, "The event has started!", Toast.LENGTH_SHORT).show()
        }

        binding.btnDirection.setOnClickListener {
            if (event.type?.lowercase() == "lắc xì" && Helper.isTimeBefore(curTime, event.start_time)){
                Toast.makeText(context, "The event has not started yet!", Toast.LENGTH_SHORT).show()
            }  else{
                // Trong Activity đầu tiên
                viewModel.chooseEvent(event)
                gameViewModel.setGame(event.type!!)
                gameViewModel.currentGame.value?.startGame(context)
            }
        }

        dialogBuilder.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

}
