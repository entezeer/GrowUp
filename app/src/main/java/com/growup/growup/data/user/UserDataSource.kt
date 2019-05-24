package com.growup.growup.data.user

import com.growup.core.callback.BaseCallback
import com.growup.growup.data.user.model.User
import com.google.firebase.auth.PhoneAuthCredential

interface UserDataSource {
    fun getUser(uid: String, callback: UserCallback)

    fun setUser(credential: PhoneAuthCredential,user: User,login:Boolean,callback: UserCallback)

    fun checkNumber(number: String, callback: UserExistCallback)

    interface UserExistCallback: BaseCallback<Boolean>

    interface UserCallback: BaseCallback<User>
}