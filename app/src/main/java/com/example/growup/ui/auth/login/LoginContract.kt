package com.example.growup.ui.auth.login

import android.content.Context
import com.example.core.mvp.BaseContract

interface LoginContract {
    interface View : BaseContract.View<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showNetworkAlert()

        fun showNumberError()

        fun showToast()

        fun openVerifyActivity(number: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun checkNetwork(context: Context)

        fun login(code: String, number: String)

        fun checkUserExist(number: String)
    }
}