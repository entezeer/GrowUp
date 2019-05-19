package com.example.growup.ui.statistic.animal

import android.content.Context
import com.example.core.mvp.BaseContract
import com.example.growup.data.statistic.model.ParentList
import com.example.growup.ui.statistic.StatisticContract

interface AnimalStatisticContract {
    interface View : BaseContract.View<Presenter> {
        fun showLoading()

        fun hideLoading()

        fun showNetworkAlert()

        fun showData(data: ArrayList<ParentList>)

        fun openDetail(parentKey: String, childKey: String)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun onStatisticItemClick(parentKey: String, childKey: String)

        fun getData()

        fun checkNetwork(context: Context)
    }
}
