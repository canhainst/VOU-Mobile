package com.example.vou_mobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.vou_mobile.R
import com.example.vou_mobile.model.registerMethod.PhoneRegister
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    private lateinit var username: String
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var phoneRegister: PhoneRegister

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Ánh xạ các thành phần giao diện
        val inputName = findViewById<TextInputEditText>(R.id.inputName)
        val inputNumberPhone = findViewById<TextInputEditText>(R.id.inputNumberPhone)
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        val termCheckbox = findViewById<RadioButton>(R.id.term)
        val signUpButton = findViewById<Button>(R.id.button)
        val backToLoginTextView = findViewById<TextView>(R.id.backToLogin)

        // Xử lý sự kiện click vào nút Đăng ký
        signUpButton.setOnClickListener {
            // Lấy thông tin từ các trường nhập liệu
            username = inputName.text.toString()
            phoneNumber = inputNumberPhone.text.toString()
            password = editTextPassword.text.toString()

            // Kiểm tra điều kiện để tiến hành đăng ký
            if (termCheckbox.isChecked && username.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty()) {
                // Tạo đối tượng PhoneRegister để xử lý đăng ký qua số điện thoại
                phoneRegister = PhoneRegister(this, phoneNumber)
                phoneRegister.sendCode()
            } else {
                // Xử lý khi điều kiện không thỏa mãn
                showToast("Please fill in all fields and agree to the terms.")
            }
        }

        // Xử lý sự kiện click vào TextView quay lại đăng nhập
        backToLoginTextView.setOnClickListener {
            val backToLoginIntent = Intent(this, SignInActivity::class.java)
            startActivity(backToLoginIntent)
            finish()
        }
    }

    // Hàm hiển thị Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}