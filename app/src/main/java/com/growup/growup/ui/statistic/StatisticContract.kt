package com.growup.growup.ui.statistic

import android.content.Context
import com.growup.core.mvp.BaseContract
import com.growup.growup.data.statistic.model.ParentList

interface StatisticContract {
    interface View: BaseContract.View<Presenter>{
        fun showLoading()

        fun hideLoading()

        fun showNetworkAlert()

        fun showData(data: ArrayList<ParentList>)

        fun openDetail(parentKey: String, childKey: String)
    }
    interface Presenter: BaseContract.Presenter<View>{

        fun onStatisticItemClick(parentKey: String, childKey: String)

        fun getData()

        fun checkNetwork(context: Context)
    }
}