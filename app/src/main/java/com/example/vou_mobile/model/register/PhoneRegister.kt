package com.example.vou_mobile.model.register

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.example.vou_mobile.activity.VerifyCodeActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneRegister(
    private val activity: Activity,
    private val phoneNumber: String,
) : RegisterMethod {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun register(callback: (Boolean, String?) -> Unit) {
        sendCode(callback)
    }
    private fun sendCode(callback: (Boolean, String?) -> Unit) {
        auth.useAppLanguage()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    signInWithPhoneAuthCredential(credential, callback)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    callback(false, e.message)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    val verifyCodeIntent = Intent(activity, VerifyCodeActivity::class.java)
                    verifyCodeIntent.putExtra("verificationId", verificationId)
                    activity.startActivity(verifyCodeIntent)
                    callback(true, null)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, callback: (Boolean, String?) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    callback(true, null)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    callback(false, task.exception?.message)
                }
            }
    }
}