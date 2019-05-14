package com.example.core.firebase

import com.google.firebase.database.FirebaseDatabase

open class FirebaseClient {
    fun getRef(ref: String) = FirebaseDatabase.getInstance().getReference(ref)
}