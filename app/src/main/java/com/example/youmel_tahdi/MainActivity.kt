package com.example.youmel_tahdi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gradproject.OnlineDataBase.DataHolder
import com.example.gradproject.OnlineDataBase.Models.User
import com.example.gradproject.OnlineDataBase.UsersDao
import com.example.youmel_tahdi.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

open class MainActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null
    private lateinit var phone : String
    private lateinit var pass : String

    private lateinit var googleSignInClient: GoogleSignInClient

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        mCurrentUser = auth.currentUser

        //CONFIGURE GOOGLE SIGN IN
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)


        sign_in.setOnClickListener {
            phone = Phone_ET?.text.toString().trim { it <= ' ' }
            pass = Pass_ET?.text.toString().trim { it <= ' ' }

            if (phone.isBlank() || phone.length <= 7) {
                Phone_ET!!.error = "Enter longer password"
                Phone_ET!!.requestFocus()
                return@setOnClickListener
            }
            if (pass.isBlank() || pass.length == 12) {
                Pass_ET?.error = "Enter a valid mobile"
                Pass_ET?.requestFocus()
                return@setOnClickListener
            }
            signIn(phone,pass)
        }

        sign_in_facebook.setOnClickListener {
            val intent = Intent(this, FaceBookAuthActivity::class.java)
            startActivity(intent)
        }

        sign_in_google.setOnClickListener {
            signInGoogle()
        }

        Reset_password.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        signup_button.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
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
                                val intent = Intent(this, NotificationActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent)
                                Log.d(TAG, "signInWithEmail:success")
                            }
                            else{    Toast.makeText(
                                activity, "get data failed.${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                            }
                        }
                    )
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(activity, NotificationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mCurrentUser != null){
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google ResetPassword Intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //google signin successfully, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }catch (e:Exception){
                // faild google auth
                Log.d(TAG,"onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAcc: begin firebase auth with google acc.")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                Log.d(TAG, "firebaseAuthWithGoogleAcc: Logged In")
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
                            Log.d(TAG, "signInWithEmail:success")
                        }
                        else{    Toast.makeText(
                            activity, "get data failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                    }
                )
            }.addOnFailureListener {
                Log.d(TAG, "logged in faild due to ${it.message}")
                Toast.makeText(
                    activity, "Logged in faild due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
