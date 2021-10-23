package com.example.gradproject.OnlineDataBase

import com.example.gradproject.OnlineDataBase.Models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot

class UsersDao {
    companion object {

        fun addUser(user: User, onCompleteListener: OnCompleteListener<Void>) {
            val userDoc = DataBase.getUsers()
                .document(user.id ?: "")
            userDoc.set(user)
                .addOnCompleteListener(onCompleteListener)
        }

        fun updateBankData(user:User,onCompleteListener: OnCompleteListener<Void>) {
            val userRef = DataBase.getUsers()
                .document(DataHolder.dataBaseUser?.id?:"")

            userRef.update("swiftCode",user.swiftCode?:"",
                "bankName",user.bankName?:"",
            "accountHolderName",user.accountHolderName?:"",
            "accountNumber",user.accountNumber?:"")
                .addOnCompleteListener(onCompleteListener)
        }

        fun updatePersonalData(user:User,onCompleteListener: OnCompleteListener<Void>) {
            val userRef = DataBase.getUsers()
                .document(DataHolder.dataBaseUser?.id?:"")

            userRef.update("idImage",user.idImage?:"",
            "drivingLicenseImage",user.drivingLicenseImage?:"",
            "passportImage",user.passportImage?:"")
                .addOnCompleteListener(onCompleteListener)
        }

        fun addUploadRecordToDb(uri: String) {
            val data = HashMap<String, Any>()
            data["imageUrl"] = uri
            DataBase.getUsers()
                .add(data)
        }

        fun getUser(userId: String, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
            DataBase.getUsers()
                .document(userId)
                .get()
                .addOnCompleteListener(onCompleteListener)
        }
    }
}