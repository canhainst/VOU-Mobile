package com.example.vou_mobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.model.Event

class MainViewModel() : ViewModel() {
    private val _favoriteEvents = MutableLiveData<List<Event>>()
    val favoriteEvents : LiveData<List<Event>> = _favoriteEvents

    fun loadFavoriteEvents(){
        //lay du lieu favorite events tu Firebase gan vao list
        val list : List<Event> = listOf(Event(null, "Shaking Game", "Phúc Long", "https://phuclong.com.vn/upload/files/a4%20htk-02.jpg", 100, "18:00 26/07/2024", "18:00 30/08/2024", 0, ""),
            Event(null, "Quiz Game", "Phúc Long", "https://phuclong.com.vn/upload/files/a4%20htk-02.jpg", 100, "18:25 28/07/2024", null, 1, ""),
            Event(null, "Thang 8", "HighLands", "https://www.highlandscoffee.com.vn/vnt_upload/news/11_2022/BR/thumbs/470_crop_Thumbnail_blog_HCO-7661-BRAND-REFRESH-DIGITAL-470X310.jpg", 100, "18:00 01/08/2024", null, 1, ""), Event(null, "Thang 8", "HighLands", "https://www.highlandscoffee.com.vn/vnt_upload/news/11_2022/BR/thumbs/470_crop_Thumbnail_blog_HCO-7661-BRAND-REFRESH-DIGITAL-470X310.jpg", 100, "18:00 01/07/2024", "", 1, ""))
        _favoriteEvents.value = list
    }

    fun removeFavoriteEvent(event: Event) {
        val currentList = _favoriteEvents.value?.toMutableList() ?: mutableListOf()
        currentList.remove(event)
        _favoriteEvents.value = currentList
    }
}