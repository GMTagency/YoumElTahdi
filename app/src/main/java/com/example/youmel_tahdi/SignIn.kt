package com.example.youmel_tahdi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val email :EditText = findViewById(R.id.mobile_number_editText)
        val pass :EditText = findViewById(R.id.password_editText)


    }
}