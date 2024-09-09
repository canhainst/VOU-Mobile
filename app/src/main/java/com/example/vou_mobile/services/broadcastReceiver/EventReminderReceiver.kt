package com.example.vou_mobile.services.broadcastReceiver

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.vou_mobile.activity.RequestPermissionsActivity

class EventReminderReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val eventName = intent.getStringExtra("event_name") ?: "Sự kiện"

        // Tạo kênh thông báo nếu cần thiết
        createNotificationChannel(context)

        // Tạo và gửi thông báo
        val notification = NotificationCompat.Builder(context, "event_channel")
            .setSmallIcon(androidx.core.R.drawable.notification_action_background) // Sửa icon phù hợp
            .setContentTitle("Sự kiện sắp diễn ra!")
            .setContentText("$eventName sẽ bắt đầu trong vài phút.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notification"
            val descriptionText = "Channel for Event Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("event_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
fun scheduleEventReminder(context: Context, id: String, eventName: String, eventTime: Long) {

    // Tính toán thời gian gửi thông báo (trước 5 phút)
    val reminderTime = eventTime - 5 * 60 * 1000

    // Tạo Intent để gửi thông báo
    val intent = Intent(context, EventReminderReceiver::class.java).apply {
        putExtra("event_name", eventName)
    }

    // Tạo PendingIntent để được kích hoạt bởi AlarmManager
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Lên lịch báo thức
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)
}

fun cancelEventReminder(context: Context, id: String) {
    val intent = Intent(context, EventReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}
