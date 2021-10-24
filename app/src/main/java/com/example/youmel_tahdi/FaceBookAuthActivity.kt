package com.example.youmel_tahdi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gradproject.OnlineDataBase.DataHolder
import com.example.gradproject.OnlineDataBase.Models.User
import com.example.gradproject.OnlineDataBase.UsersDao
import com.example.youmel_tahdi.base.BaseActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.OnCompleteListener
import java.util.*


class FaceBookAuthActivity : BaseActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth

        callbackManager = CallbackManager.Factory.create()


        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"));
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"));
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    handleFacebookAccessToken(loginResult!!.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(baseContext, "تم الألغاء الرجاء المحاولة بطريقة أخري.",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(baseContext, "حدث خطأ. الرجاء المحاولة مرة أخري.",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    UsersDao.getUser(
                        userId =auth.currentUser?.uid?:"",
                        onCompleteListener = OnCompleteListener {
                            if (it.isSuccessful){
                                val dataBaseUser=it.result?.toObject(User::class.java)
                                DataHolder.dataBaseUser=dataBaseUser
                                DataHolder.authUser=auth.currentUser
                                // Sign in success, update UI with the signed-in user's information
                                val intent = Intent(this, SaveDetailsActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent)

                            }
                            else{    Toast.makeText(
                                activity, "get data failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            }
                        }
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


    public override fun onStart() {
        super.onStart()
    }


    companion object {
        private const val TAG = "FacebookLogin"
    }

}