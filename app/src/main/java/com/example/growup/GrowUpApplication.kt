package com.example.growup

import android.app.Application
import com.example.growup.models.Products
import com.example.growup.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GrowUpApplication: Application(){
    companion object {
        lateinit var mAuth: FirebaseAuth
        lateinit var mUserRef: DatabaseReference
        lateinit var mMarketRef: DatabaseReference
        lateinit var mSoldRef: DatabaseReference
        lateinit var mStatisticRef: DatabaseReference
        lateinit var mAnimalStatisticRef: DatabaseReference
        lateinit var mStorage: StorageReference
        lateinit var mUserData: User
        lateinit var mUserList: ArrayList<User>
        var productsData: ArrayList<Products> = ArrayList()
    }
    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        mUserRef = FirebaseDatabase.getInstance().getReference("users")
        mMarketRef = FirebaseDatabase.getInstance().getReference("market")
        mSoldRef = mMarketRef.child("sold")
        mStatisticRef = FirebaseDatabase.getInstance().getReference("statistic")
        mAnimalStatisticRef = FirebaseDatabase.getInstance().getReference("animalStatistic")
        mStorage = FirebaseStorage.getInstance().getReference()
    }
}