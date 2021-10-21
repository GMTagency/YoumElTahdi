package com.example.youmel_tahdi

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val email :EditText = findViewById(R.id.mobile_number_editText)
        val pass :EditText = findViewById(R.id.password_editText)


    }
}