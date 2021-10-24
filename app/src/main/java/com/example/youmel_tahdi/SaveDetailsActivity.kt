package com.example.youmel_tahdi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gradproject.OnlineDataBase.DataHolder
import com.example.gradproject.OnlineDataBase.Models.User
import com.example.gradproject.OnlineDataBase.UsersDao
import com.example.youmel_tahdi.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SaveDetailsActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null

    private lateinit var userId: String

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_details)

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

            userId =auth.currentUser?.uid?:""
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
                        "save data failed..try again.${it.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                    signup_button_signup.isClickable = true
                }
            })



        }
    }
}