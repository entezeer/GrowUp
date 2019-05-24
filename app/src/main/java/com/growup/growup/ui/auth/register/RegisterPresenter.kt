package com.growup.growup.ui.auth.register

import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.model.User

class RegisterPresenter(private val mUserDataSource: UserDataSource): RegisterContract.Presenter {
    override fun register(code: String, number: String, user: User) {
        mView?.showLoading()
        var mNumber = number
        if (mNumber.isEmpty() || mNumber.length < 9) {
            mView?.showNumberError()
            return
        }
        if (mNumber[0].equals('0') && code.equals("996")) {
            mNumber = mNumber.substring(1, mNumber.length)
        }
        checkUserExist("+${code + mNumber}", user)
    }

    override fun checkUserExist(number: String, user: User) {
        mUserDataSource.checkNumber(number, object : UserDataSource.UserExistCallback{
            override fun onSuccess(result: Boolean) {
                if (!result) {
                    mView?.hideLoading()
                    mView?.openVerifyActivity(number, user)
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