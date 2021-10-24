package com.example.gradproject.OnlineDataBase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.ref.Reference

class DataBase {
    companion object{
        val USERS_REF= "USERS"
        val ADMINS_REF= "ADMINS"
        val db = Firebase.firestore

        fun getUsers():CollectionReference{
            return db.collection(USERS_REF)
        }

        fun getAdmins():CollectionReference{
            return db.collection(ADMINS_REF)
        }

    }
}