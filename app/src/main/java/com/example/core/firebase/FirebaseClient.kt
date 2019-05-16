package com.example.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

open class FirebaseClient {
    fun getRef(ref: String) = FirebaseDatabase.getInstance().getReference(ref)

    fun getStorage() = FirebaseStorage.getInstance().reference

    fun getAuth() = FirebaseAuth.getInstance()
}