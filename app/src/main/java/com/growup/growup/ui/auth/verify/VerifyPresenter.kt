package com.growup.growup.ui.auth.verify

import android.content.Context
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.model.User
import com.growup.growup.ui.auth.register.RegisterContract

class VerifyPresenter(private val mUserDataSource: UserDataSource): RegisterContract.Presenter {
    override fun checkNetwork(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun register(code: String, number: String, user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkUserExist(number: String, user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachView(view: RegisterContract.View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}