package com.example.growup.ui.auth.register

import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.data.user.UserDataSource
import com.example.growup.data.user.model.User

class RegisterPresenter(private val mUserDataSource: UserDataSource): RegisterContract.Presenter {
    override fun register(code: String, number: String,password: String, user: User) {

    }

    override fun checkUserExist(number: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mView: RegisterContract.View? = null

    override fun checkNetwork(context: Context) {
        if (!InternetUtil.checkInternet(context)){
            mView?.showNetworkAlert()
        }
    }

    override fun attachView(view: RegisterContract.View) {
        mView = view
        view.attachPresenter(this)
    }

    override fun detachView() {
        mView = null
    }
}