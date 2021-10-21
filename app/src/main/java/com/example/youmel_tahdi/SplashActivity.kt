package com.example.youmel_tahdi

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar




class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val spinner = ProgressBar(this, null, android.R.attr.progressBarStyle)

        spinner.indeterminateDrawable.setColorFilter(-0x10000, PorterDuff.Mode.MULTIPLY)

        StartNewActivity()    }

    private fun StartNewActivity() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}