package com.example.youmel_tahdi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private lateinit var mail : String

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = Firebase.auth

        Reset_password_button.setOnClickListener {
            mail = reset_mail_field?.text.toString().trim { it <= ' '}

                if (mail.isBlank()) {
                    reset_mail_field!!.error = "Enter invalid E-mail"
                    reset_mail_field!!.requestFocus()
                    return@setOnClickListener
                }


                auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this, "تم أرسال الرقم السري الخاص بك لبريدك الشخصي", Toast.LENGTH_LONG
                            ).show()
                        }else{
                            Toast.makeText(
                                this, it.exception?.message, Toast.LENGTH_LONG
                            )
                        }
                    }

    }
        Back_to_home.setOnClickListener {
            finish()
        }



    }
}