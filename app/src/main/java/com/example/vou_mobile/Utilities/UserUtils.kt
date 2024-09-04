package com.example.vou_mobile.Utilities

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object UserUtils {
    private var auth: FirebaseAuth = Firebase.auth
    val userID = auth.currentUser!!.uid

    fun getUuidById(callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference

        // Tham chiếu đến user cụ thể
        val userRef = database.child(userID)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Lấy giá trị UUID từ snapshot
                val uuid = snapshot.child("uuid").getValue(String::class.java)

                // Gọi callback với giá trị UUID
                callback(uuid)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần thiết
                println("Lỗi khi đọc dữ liệu: ${error.message}")
                callback(null)
            }
        })
    }
}