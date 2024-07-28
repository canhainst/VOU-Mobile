package com.example.vou_mobile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vou_mobile.R
import com.example.vou_mobile.model.register.PhoneRegister
import com.example.vou_mobile.model.register.RegisterMethod
import com.example.vou_mobile.viewModel.AuthViewModel
import com.google.firebase.auth.PhoneAuthProvider

class VerifyCodeActivity : AppCompatActivity() {
    private lateinit var codeViews: List<TextView>
    private var currentInputIndex = 0
    private var codeInputted = ""
    private lateinit var verificationId: String
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        codeViews = listOf(
            findViewById(R.id.code1),
            findViewById(R.id.code2),
            findViewById(R.id.code3),
            findViewById(R.id.code4),
            findViewById(R.id.code5),
            findViewById(R.id.code6)
        )

        verificationId = intent.getStringExtra("verificationId") ?: ""

        findViewById<Button>(R.id.BtnConfirm).setOnClickListener {
            codeInputted = getCodeFromViews()
            if (codeInputted.length == 6) {
                val credential = PhoneAuthProvider.getCredential(verificationId, codeInputted)
                val phoneRegister = PhoneRegister(this, "") // Empty phone number since it is not needed here
                authViewModel.registerWithMethod(object : RegisterMethod {
                    override fun register(callback: (Boolean, String?) -> Unit) {
                        phoneRegister.signInWithPhoneAuthCredential(credential, callback)
                    }
                })
                authViewModel.registerResult.observe(this) { result ->
                    val (isSuccessful, message) = result
                    if (isSuccessful) {
                        showToast("Registration successful")
                        navigateToUpdateAccount()
                    } else {
                        showToast("Registration failed: $message")
                    }
                }
            }
        }

        val layoutVerify = findViewById<LinearLayout>(R.id.layoutVerify)
        layoutVerify.setOnClickListener {
            openKeyboard()
        }

        layoutVerify.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 || keyCode == KeyEvent.KEYCODE_DEL)) {
                handleKeyPress(keyCode, codeViews)
                true
            } else {
                false
            }
        }
    }
    private fun openKeyboard() {
        val layoutVerify = findViewById<LinearLayout>(R.id.layoutVerify)
        layoutVerify.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(layoutVerify, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun handleKeyPress(keyCode: Int, codeViews: List<TextView>) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            // Xử lý khi người dùng nhấn nút BACKSPACE
            if (currentInputIndex > 0) {
                currentInputIndex--
                codeViews[currentInputIndex].text = ""
            }
        } else if (keyCode in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9) {
            // Xử lý khi người dùng nhấn các phím số
            val inputChar = (keyCode - KeyEvent.KEYCODE_0).toString()
            if (currentInputIndex < codeViews.size) {
                codeViews[currentInputIndex].text = inputChar
                currentInputIndex++
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val layoutVerify = findViewById<LinearLayout>(R.id.layoutVerify)
        layoutVerify.requestFocus()
    }
    private fun getCodeFromViews(): String {
        val stringBuilder = StringBuilder()

        // Duyệt qua danh sách các TextView và lấy nội dung của từng TextView
        for (codeView in codeViews) {
            stringBuilder.append(codeView.text)
        }

        return stringBuilder.toString()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun navigateToUpdateAccount() {
        val updateAccount = Intent(this, UpdateAccount::class.java)
        startActivity(updateAccount)
        finish()
    }
}