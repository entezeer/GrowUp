package com.growup.growup.ui.auth.register

import android.content.Context
import com.growup.core.mvp.BaseContract
import com.growup.growup.data.user.model.User

interface RegisterContract {
    interface View : BaseContract.View<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showNetworkAlert()

        fun showNumberError()

        fun showToast()

        fun openVerifyActivity(number: String, user: User)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun checkNetwork(context: Context)

        fun register(code: String, number: String, user: User)

        fun checkUserExist(number: String, user: User)
    }
}