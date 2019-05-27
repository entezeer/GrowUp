package com.growup.growup.data.user.remote

import com.growup.core.firebase.FirebaseClient
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.model.User
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserRemote : FirebaseClient(), UserDataSource {



    companion object {
        private var INSTANCE: UserRemote? = null
        fun getInstance(): UserRemote {
            if (INSTANCE == null) {
                INSTANCE = UserRemote()
            }
            return INSTANCE!!
        }
    }

    private val userRef = getRef(UserRemoteConstants.USER_KEY)
    private val userAuth = getAuth()

    override fun getUser(uid: String, callback: UserDataSource.UserCallback) {
        userRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                callback.onFailure(databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue(User::class.java)?.let {
                    callback.onSuccess(it)
                }
            }
        })
    }

    override fun setUser(credential:PhoneAuthCredential,user: User,login: Boolean,callback: UserDataSource.UserCallback) {
        userAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (!login) {
                        userRef.child(userAuth.currentUser?.uid!!)
                            .setValue(user).addOnCompleteListener { it1 ->
                                if (it1.isSuccessful) {
                                    callback.onSuccess(user)
                                }
                            }
                    }
                    else{
                        callback.onSuccess(user)
                    }
                } else {
                    callback.onFailure("")
                }
            }
    }

    override fun checkNumber(number: String, callback: UserDataSource.UserExistCallback) {
        userRef.orderByChild(UserRemoteConstants.PHONE_NUMBER).equalTo(number)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    callback.onFailure(databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        callback.onSuccess(true)
                    } else {
                        // user with this number doesn't exist ->
                        callback.onSuccess(false)
                    }
                }
            })
    }
}