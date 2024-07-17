package com.example.vou_mobile.ViewModels

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.vou_mobile.R
import com.example.vou_mobile.RegisterMethod.PhoneRegister

class VerifyCodeActivity : AppCompatActivity() {
    private lateinit var codeViews: List<TextView>
    private var currentInputIndex = 0
    private var codeInputted = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        codeViews = listOf(
            findViewById<TextView>(R.id.code1),
            findViewById(R.id.code2),
            findViewById(R.id.code3),
            findViewById(R.id.code4),
            findViewById(R.id.code5),
            findViewById(R.id.code6)
        )

        findViewById<Button>(R.id.BtnConfirm).setOnClickListener {
            codeInputted = getCodeFromViews()
            println(codeInputted)
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
}