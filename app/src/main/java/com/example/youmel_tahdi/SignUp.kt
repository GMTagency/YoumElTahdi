package com.example.youmel_tahdi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.youmel_tahdi.R
import com.facebook.CallbackManager

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    var emailET : EditText? = null
    var passwordET : EditText? =null

    lateinit var email : String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val callbackManager = CallbackManager.Factory.create();


        auth = Firebase.auth

        emailET = findViewById(R.id.First_name_editText)
        passwordET =findViewById(R.id.password_editText)
        var createEmail : Button = findViewById(R.id.register_button)
        var createFacebook : Button = findViewById(R.id.register_facebook)

        email= emailET?.text.toString().trim { it <= ' ' }
        password= passwordET?.text.toString().trim { it <= ' ' }

        createEmail.setOnClickListener {
            createAccount(email,password)
        }

        createFacebook.setOnClickListener {
            val intent = Intent(this, FaceBookAuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }


}