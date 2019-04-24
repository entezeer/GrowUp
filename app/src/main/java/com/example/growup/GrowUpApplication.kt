package com.example.growup

import android.app.Application
import com.example.growup.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GrowUpApplication: Application(){
    companion object {
        lateinit var mAuth: FirebaseAuth
        lateinit var mUserRef: DatabaseReference
        lateinit var mUserData: User
        lateinit var mUserList: ArrayList<User>
    }
    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        mUserRef = FirebaseDatabase.getInstance().getReference("users")
    }
}