package com.example.vou_mobile.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.HorizontalEventsAdapter
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import com.example.vou_mobile.services.EventService
import com.example.vou_mobile.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EventViewModelProviderSingleton {
    private var eventViewModel: EventViewModel? = null

    fun getEventViewModel(): EventViewModel {
        if (eventViewModel == null) {
            eventViewModel = EventViewModel()
        }
        return eventViewModel!!
    }
}

class EventViewModel() : ViewModel() {
    private val _favoriteEvents = MutableLiveData<List<Event>>()
    val favoriteEvents : LiveData<List<Event>> = _favoriteEvents
    private val _events = MutableLiveData<List<Event>>()
    val events : LiveData<List<Event>> = _events
    private val _curEvent = MutableLiveData<Event>()
    val curEvent: LiveData<Event> = _curEvent

    fun loadAllEvents(){
        _events.value = emptyList()
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.getAllEvent().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _events.value = response.body()!!
                    Log.d("EventViewModel", response.body().toString())
                } else{
                    _events.value = emptyList()
                    Log.e("EventViewModel", "Empty or failed response.")
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                _events.value = emptyList()
                Log.e("EventViewModel", "Failed to fetch events: ${t.message}")
            }
        })
    }
    fun loadFavoriteEvents(userId: String) {
        // Tạo Retrofit instance và service
        val api = RetrofitClient.instance.create(EventService::class.java)

        // Gọi API để lấy danh sách sự kiện yêu thích
        api.getAllFavorite(userId).enqueue(object : Callback<List<FavEvents>> {
            override fun onResponse(call: Call<List<FavEvents>>, response: Response<List<FavEvents>>) {
                if (response.isSuccessful && response.body() != null) {
                    val favEvents = response.body()!!

                    // Lấy danh sách các eventId từ favEvents
                    val eventIds = favEvents.map { it.id_event }

                    // Nếu danh sách eventIds rỗng, cập nhật UI ngay lập tức
                    if (eventIds.isEmpty()) {
                        _favoriteEvents.value = emptyList()
                        return
                    }

                    // Tạo danh sách sự kiện rỗng để cập nhật sau
                    val eventsList = mutableListOf<Event>()

                    // Đếm số lượng sự kiện cần lấy để xử lý đồng bộ hóa
                    val totalEvents = eventIds.size
                    var eventsFetched = 0

                    // Đối với mỗi eventId, gọi API để lấy chi tiết sự kiện
                    for (eventId in eventIds) {
                        api.getEventByID(eventId).enqueue(object : Callback<Event> {
                            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                                if (response.isSuccessful && response.body() != null) {
                                    val event = response.body()!!

                                    // Thêm sự kiện vào danh sách
                                    eventsList.add(event)
                                } else {
                                    Log.d("API", "Failed to fetch event details for ID: $eventId")
                                }

                                // Kiểm tra xem tất cả các sự kiện đã được lấy chưa
                                eventsFetched++
                                if (eventsFetched == totalEvents) {
                                    // Cập nhật UI hoặc thực hiện các hành động khác sau khi có dữ liệu
                                    Log.d("API", "All event details fetched.${eventsList}")
                                    _favoriteEvents.value = eventsList
                                }
                            }

                            override fun onFailure(call: Call<Event>, t: Throwable) {
                                Log.d("API", "Error fetching event details: ${t.message}")
                                eventsFetched++
                                if (eventsFetched == totalEvents) {
                                    _favoriteEvents.value = eventsList
                                }
                            }
                        })
                    }
                } else {
                    Log.d("API", "No favorite events found or failed to fetch.")
                    _favoriteEvents.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<FavEvents>>, t: Throwable) {
                Log.d("API", "Error: ${t.message}")
                _favoriteEvents.value = emptyList()
            }
        })
    }

    fun removeFavoriteEvent(event: Event, userID: String,  callback: (Boolean) -> Unit) {
        val currentList = _favoriteEvents.value?.toMutableList() ?: mutableListOf()
        currentList.remove(event)

        //update postgre
        val api = RetrofitClient.instance.create(EventService::class.java)
//        api.deleteFavorite(event.id!!, userID).enqueue(object : Callback<Void> {
        api.deleteFavorite("c4fb5c4d-4ef7-4c43-b978-4bfab379dce1", userID).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.d("API", "Failed to delete favorite event.")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("API", "Error: ${t.message}")
                callback(false)
            }
        })
        _favoriteEvents.value = currentList
    }

    fun addFavoriteEvent(FavEvent: FavEvents, callback: (Boolean) -> Unit) {
        //update postgre
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.addFavorite(FavEvent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    Log.d("Err addFavoriteEvent", "Error: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                callback(false)
            }
        })
        loadFavoriteEvents(FavEvent.id_user)
    }

    fun chooseEvent(event: Event){
        _curEvent.value = event
    }
}