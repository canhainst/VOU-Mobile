package com.example.vou_mobile.classData
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String?,
    var pwd: String?,
    var email: String?,
    var dob: String?,
    var gender: String?,
    var avtUrl: String?
)