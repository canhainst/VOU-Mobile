package com.example.vou_mobile.helper

import com.example.vou_mobile.model.Event
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {
    private const val dateTimeFormat: String = "05:37 dd/MM/yyyy"
    private fun convertDateString(dateString: String): String {
        // Định dạng đầu vào
        val fullFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val shortFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            // Kiểm tra nếu chuỗi đã ở định dạng HH:mm dd/MM/yyyy
            fullFormat.parse(dateString)
            dateString // Trả về chuỗi gốc nếu đã đúng định dạng
        } catch (e: Exception) {
            try {
                // Chuyển đổi từ định dạng dd/MM/yyyy sang HH:mm dd/MM/yyyy
                val date = shortFormat.parse(dateString)
                fullFormat.format(date!!)
            } catch (e: Exception) {
                try {
                    // Chuyển đổi từ định dạng yyyy-MM-dd sang HH:mm dd/MM/yyyy
                    val date = isoFormat.parse(dateString)
                    fullFormat.format(date!!)
                } catch (e: Exception) {
                    "Invalid date format" // Trả về chuỗi thông báo lỗi nếu không khớp với định dạng nào
                }
            }
        }
    }

    fun fixEventTime(event: Event): Event {
        // Định dạng ngày giờ đầu vào và đầu ra
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())

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
        val outputFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())

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
        println("$time - $time2")
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

    fun getTimeRangeString(event: Event): String{
        var str = ""
        when (event.type?.lowercase()) {
            "lắc xì" -> {
                str = "${event.start_time} - ${event.end_time}"
            }
            "quiz" -> {
                val updateEvent = fixEventTime(event)
                str = "${updateEvent.start_time}"
            }
        }
        return str
    }


}