package com.example.gradproject.OnlineDataBase

import com.example.gradproject.OnlineDataBase.Models.User
import com.google.firebase.auth.FirebaseUser

class DataHolder {
    companion object{
        var dataBaseUser:User?=null
        var authUser:FirebaseUser?=null
        var carId:String?=null

    }
}