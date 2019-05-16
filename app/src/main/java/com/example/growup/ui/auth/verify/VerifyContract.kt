package com.example.growup.ui.auth.verify

import android.content.Context
import com.example.core.mvp.BaseContract

interface VerifyContract {
    interface View : BaseContract.View<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showNetworkAlert()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun checkNetwork(context: Context)
    }
}