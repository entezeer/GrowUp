package com.example.growup.data.user

import com.example.growup.data.user.model.User
import com.example.growup.data.user.remote.UserRemote
import com.google.firebase.auth.PhoneAuthCredential

class UserRepository(private val remoteSource: UserRemote): UserDataSource {
    companion object {

        private var INSTANCE: UserDataSource? = null
        fun getInstance(remoteSource: UserRemote): UserDataSource {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(remoteSource)
            }
            return INSTANCE!!
        }
    }

    override fun getUser(uid: String, callback: UserDataSource.UserCallback) {
            remoteSource.getUser(uid, object : UserDataSource.UserCallback{
                override fun onSuccess(result: User) {
                    callback.onSuccess(result)
                }

                override fun onFailure(message: String) {
                    callback.onFailure(message)
                }
            })
    }

    override fun setUser(
        credential: PhoneAuthCredential,
        user: User,
        login: Boolean,
        callback: UserDataSource.UserCallback
    ) {
        remoteSource.setUser(credential,user,login,object : UserDataSource.UserCallback{
            override fun onSuccess(result: User) {
                callback.onSuccess(result)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

        })
    }

    override fun checkNumber(number: String, callback: UserDataSource.UserExistCallback) {
        remoteSource.checkNumber(number,object : UserDataSource.UserExistCallback{
            override fun onSuccess(result: Boolean) {
                callback.onSuccess(result)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

        })
    }
}