package com.example.vou_mobile.model.games

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.example.vou_mobile.activity.ShakingGameActivity

class ShakingGame(private val eventId: String): Game {
    override fun startGame(context: Context) {
        val intent = Intent(context, ShakingGameActivity::class.java).apply {
            putExtra("idEvent", eventId) // Truyền idEvent vào Intent
        }
        context.startActivity(intent)
    }

    override fun endGame(context: Context) {
        if (context is AppCompatActivity) {
            context.finish()
        }
    }
}

class ShakeDetector(private val onShakeListener: OnShakeListener) : SensorEventListener {

    interface OnShakeListener {
        fun onShake()
    }

    private var lastShakeTime: Long = 0
    private val shakeThresholdGravity = 2.7f
    private val shakeSlopTimeMs = 500

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // Tính tổng lực gia tốc trên tất cả các trục
            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > shakeThresholdGravity) {
                val now = System.currentTimeMillis()
                if (lastShakeTime + shakeSlopTimeMs > now) {
                    return
                }

                lastShakeTime = now
                onShakeListener.onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Không sử dụng
    }
}
