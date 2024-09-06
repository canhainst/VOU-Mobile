package com.example.vou_mobile.activity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

class RequestPermissionsActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with notifications
                // You might need to trigger the notification again here
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}