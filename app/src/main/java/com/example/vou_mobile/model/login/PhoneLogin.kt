package com.example.vou_mobile.model.login

class PhoneLogin(private val phoneNumber: String) : LoginMethod {
    override fun login(callback: (Boolean, String?) -> Unit) {
        // Implement logic đăng nhập bằng số điện thoại
        // Sau khi đăng nhập xong, gọi callback(true, "Success") nếu thành công, hoặc callback(false, "Error message") nếu thất bại
    }
}
