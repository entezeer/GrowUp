package com.example.growup

import android.app.Application
import com.example.growup.data.market.model.Products
import com.example.growup.data.user.model.User
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
        lateinit var mSailRef: DatabaseReference
        lateinit var mStatisticRef: DatabaseReference
        lateinit var mAnimalStatisticRef: DatabaseReference
        lateinit var mStorage: StorageReference
        lateinit var mUserData: User
        var productsData: ArrayList<Products> = ArrayList()
    }
    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        mUserRef = FirebaseDatabase.getInstance().getReference("users")
        mMarketRef = FirebaseDatabase.getInstance().getReference("market")
        mSoldRef = mMarketRef.child("onSold")
        mSailRef = mMarketRef.child("onSail")
        mStatisticRef = FirebaseDatabase.getInstance().getReference("statistic")
        mAnimalStatisticRef = FirebaseDatabase.getInstance().getReference("animalStatistic")
        mStorage = FirebaseStorage.getInstance().getReference()
    }
}