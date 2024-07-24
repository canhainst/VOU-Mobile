package com.example.vou_mobile.Utilities

import com.example.vou_mobile.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

object UserUtils {
    private var auth: FirebaseAuth = Firebase.auth
    val userID = auth.currentUser!!.uid
    fun addNewUser(user: User){
        val database = FirebaseDatabase.getInstance().reference

        // Save the user information under the "users" node
        database.child("users").child(userID).setValue(user)
            .addOnSuccessListener {
                // Handle success (optional)
                println("User added successfully.")
            }
            .addOnFailureListener { e ->
                // Handle failure (optional)
                println("Failed to add user: ${e.message}")
            }
    }
}