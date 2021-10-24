package com.example.youmel_tahdi

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gradproject.OnlineDataBase.DataHolder
import com.example.gradproject.OnlineDataBase.Models.User
import com.example.gradproject.OnlineDataBase.UsersDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.youmel_tahdi.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.Exception

class SignUp : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null

    private lateinit var userId: String

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //CONFIGURE GOOGLE SIGN IN
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)

        sign_up_google.setOnClickListener {
            signInGoogle()
        }

        sign_up_facebook.setOnClickListener {
            val intent = Intent(this, FaceBookAuthActivity::class.java)
            startActivity(intent)
        }



        signup_button_signup.setOnClickListener OnClickListener@{
            signup_button_signup.isClickable = false
            ButtonEffect(signup_button_signup)

            var fullName = full_name?.text.toString().trim { it <= ' ' }
            var eMail = email_field_signup?.text.toString().trim { it <= ' ' }
            var phoneNumber = phone_number_signup?.text.toString().trim { it <= ' ' }
            var birthDay = birthday_signup?.text.toString().trim { it <= ' ' }
            var idCard = id_signup?.text.toString().trim { it <= ' ' }
            var fullAddress = full_address?.text.toString().trim { it <= ' ' }
            var jobfield = job_field?.text.toString().trim { it <= ' ' }
            var city = mohafza_field.selectedItem.toString();
            var markz = markaz_field.selectedItem.toString();
            var password = password_field_signup?.text.toString().trim { it <= ' ' }


            if (fullName.isBlank()) {
                full_name!!.error = "Enter a valid mobile"
                full_name!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }

            if (eMail.isBlank()) {
                email_field_signup!!.error = "Enter a valid mobile"
                email_field_signup!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }

            if (phoneNumber.isBlank() || phoneNumber.length == 12) {
                phone_number_signup!!.error = "Enter your real location"
                phone_number_signup!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }

            if (birthDay.isBlank()) {
                birthday_signup!!.error = "Enter your real location"
                birthday_signup!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }
            if (idCard.isBlank() || phoneNumber.length == 15) {
                id_signup!!.error = "Enter your real location"
                id_signup!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }
            if (fullAddress.isBlank() || phoneNumber.length == 12) {
                full_address!!.error = "Enter your real location"
                full_address!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }

            if (jobfield.isBlank() || phoneNumber.length == 12) {
                job_field!!.error = "Enter your real location"
                job_field!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }

            if (password.isBlank() || password.length <= 7) {
                password_field_signup!!.error = "Enter longer password"
                password_field_signup!!.requestFocus()
                signup_button_signup.isClickable = true
                return@OnClickListener
            }
                    auth.createUserWithEmailAndPassword(eMail, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                userId = task.result?.user?.uid ?: ""
                                val newUser = User()
                                newUser.id = userId
                                newUser.fullName = fullName
                                newUser.eMail = eMail
                                newUser.phoneNumber = phoneNumber
                                newUser.birthDay = birthDay
                                newUser.idNumber = idCard
                                newUser.fullAddress = fullAddress
                                newUser.job = jobfield
                                newUser.city = city
                                newUser.markaz = markz
                                UsersDao.addUser(newUser, OnCompleteListener {
                                    if (it.isSuccessful) {
                                        DataHolder.dataBaseUser = newUser
                                        DataHolder.authUser = auth.currentUser
                                        // Sign in success, update UI with the signed-in user's information
                                        val intent = Intent(this, NotificationActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "save data failed..try again.${task.exception}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        signup_button_signup.isClickable = true
                                    }
                                })
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    activity, "Authentication failed.${task.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                signup_button_signup.isClickable = true
                            }
                        }
                }

        }


    public override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        mCurrentUser = auth.currentUser
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mCurrentUser != null){
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
        }


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAGGOogle, "firebaseAuthWithGoogleAcc: begin firebase auth with google acc.")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                Log.d(TAGGOogle, "firebaseAuthWithGoogleAcc: Logged In")
                UsersDao.getUser(
                    userId =auth.currentUser?.uid?:"",
                    onCompleteListener = OnCompleteListener {
                        if (it.isSuccessful){
                            val dataBaseUser=it.result?.toObject(User::class.java)
                            DataHolder.dataBaseUser=dataBaseUser
                            DataHolder.authUser=auth.currentUser
                            // Sign in success, update UI with the signed-in user's information
                            val intent = Intent(this, NotificationActivity::class.java)
                            startActivity(intent)
                            Log.d(TAGAuth, "signInWithEmail:success")
                        }
                        else{    Toast.makeText(
                            activity, "get data failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                    }
                )
            }.addOnFailureListener {
                Log.d(TAGGOogle, "logged in faild due to ${it.message}")
                Toast.makeText(
                    activity, "Logged in faild due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            Log.d(TAGGOogle, "onActivityResult: Google ResetPassword Intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //google signin successfully, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }catch (e: Exception){
                // faild google auth
                Log.d(TAGGOogle,"onActivityResult: ${e.message}")
            }
        }
    }


    companion object {
        private const val TAGAuth = "EmailPassword"
        private const val RC_SIGN_IN = 100
        private const val TAGGOogle = "GOOGLE_SIGN_IN_TAG"
    }
}







