package com.example.vou_mobile.model
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String,
    var full_name: String,
    var user_name: String,
    var password: String,
    var avatar: String?,
    var dob: String?,
    var gender: String?,
    var fb_acc: String?,
    var email: String?,
    var phone: String,
    var type: String,
    var status: String,
    var time_update: String
)