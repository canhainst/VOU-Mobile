package com.example.vou_mobile.helper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vou_mobile.model.Event
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Helper {
    fun convertDateString(dateString: String): String {
        val fullFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val shortFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return try {
            fullFormat.parse(dateString)
            dateString // Chuỗi đã ở định dạng HH:mm dd/MM/yyyy, trả về nguyên bản
        } catch (e: Exception) {
            try {
                val date = shortFormat.parse(dateString)
                fullFormat.format(date) // Chuyển đổi thành định dạng HH:mm dd/MM/yyyy
            } catch (e: Exception) {
                "Invalid date format"
            }
        }
    }

    fun fixEventTime(event: Event): Event {
        // Định dạng ngày giờ đầu vào và đầu ra
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("09:35 dd/MM/yyyy", Locale.getDefault())

        // Hàm giúp chuyển đổi chuỗi ngày giờ
        fun formatDate(dateStr: String): String {
            return try {
                val date = inputFormat.parse(dateStr) ?: return ""
                outputFormat.format(date)
            } catch (e: ParseException) {
                ""
            }
        }

        val newStartTime = formatDate(event.start_time!!)
        val newEndTime = event.end_time?.let { formatDate(it) }

        return event.copy(
            start_time = newStartTime,
            end_time = newEndTime
        )
    }

    fun fixTime(time: String): String{
        // Định dạng ngày giờ đầu vào và đầu ra
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("09:35 dd/MM/yyyy", Locale.getDefault())

        // Hàm giúp chuyển đổi chuỗi ngày giờ
        fun formatDate(dateStr: String): String {
            return try {
                val date = inputFormat.parse(dateStr) ?: return ""
                outputFormat.format(date)
            } catch (e: ParseException) {
                ""
            }
        }

        return formatDate(time)
    }

    //kiem tra time1 có sau time2 khong
    fun isTimeAfter(time: String?, time2:String?): Boolean {

        val Time = convertDateString(time!!)
        val Time2 = convertDateString(time2!!)
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val t1: Date? = Time.let { dateFormat.parse(it) }
        val t2: Date? = Time2.let { dateFormat.parse(it) }
        return try {
            t1!!.after(t2)
        } catch (e: Exception) {
            false
        }
    }

    fun isTimeBefore(time: String?, time2:String?): Boolean {
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val Time = convertDateString(time!!)
        val Time2 = convertDateString(time2!!)
        val t1: Date? = Time.let { dateFormat.parse(it) }
        val t2: Date? = Time2.let { dateFormat.parse(it) }
        return try {
            t1!!.before(t2)
        } catch (e: Exception) {
            false
        }
    }

    fun isTimeInRange(time:String?, startTime: String?, endTime: String?): Boolean{
        return try {
            !(isTimeBefore(time, startTime) || isTimeAfter(time, endTime))
        } catch (e: Exception) {
            false
        }
    }

    //chuyen tu dang Date sang chuoi dang HH:mm dd/MM/yyyy
    fun dateToString(date: Date): String?{
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun stringToDate(date: String): Date? {
        val time = convertDateString(date)
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(time)
    }

    fun getTimeRangeString(event: Event): String?{
        return when (event.type?.lowercase()) {
            "lắc xì" -> "${event.start_time} - ${event.end_time}"
            "quiz" -> event.start_time
            else -> ""
        }
    }


}