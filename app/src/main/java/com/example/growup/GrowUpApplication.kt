package com.example.growup

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GrowUpApplication: Application(){
    companion object {
        lateinit var mAuth: FirebaseAuth
        lateinit var mTechRef: DatabaseReference
        lateinit var mUserRef: DatabaseReference


    }
    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        mUserRef = FirebaseDatabase.getInstance().getReference("users")

    }
}