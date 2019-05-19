package com.example.growup.ui.auth.login

import android.content.Context
import android.util.Log
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.data.user.UserDataSource

class LoginPresenter(private val mUserDataSource: UserDataSource) : LoginContract.Presenter {

    private var mView: LoginContract.View? = null

    override fun login(code: String, number: String) {
        mView?.showLoading()
        var mNumber = number
        if (mNumber.isEmpty() || mNumber.length < 9) {
            mView?.showNumberError()
            return
        }
        if (mNumber[0].equals('0') && code.equals("996")) {
            mNumber = mNumber.substring(1, mNumber.length)
        }
        checkUserExist("+${code + mNumber}")
    }

    override fun checkNetwork(context: Context) {
        if (!InternetUtil.checkInternet(context)){
            mView?.showNetworkAlert()
        }
    }

    override fun checkUserExist(number: String) {
        mUserDataSource.checkNumber(number, object : UserDataSource.UserExistCallback {
            override fun onSuccess(result: Boolean) {
                if (result) {
                    mView?.hideLoading()
                    mView?.openVerifyActivity(number)
                } else {
                    mView?.hideLoading()
                    mView?.showToast()
                }
            }

            override fun onFailure(message: String) {
                mView?.hideLoading()
                mView?.showToast()
            }

        })
    }

    override fun attachView(view: LoginContract.View) {
        mView = view
        view.attachPresenter(this)
    }

    override fun detachView() {
        mView = null
    }
}