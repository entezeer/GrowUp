package com.example.growup.ui.statistic

import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.data.statistic.StatisticDataSource
import com.example.growup.data.statistic.model.ParentList

class StatisticPresenter(private val mStatisticDataSource: StatisticDataSource): StatisticContract.Presenter {

    private var mView: StatisticContract.View? = null

    override fun getData() {
        mView?.showLoading()
        mStatisticDataSource.getStatistic(object : StatisticDataSource.RequestCallback{
            override fun onSuccess(result: ArrayList<ParentList>) {
                mView?.showData(result)
                mView?.hideLoading()
            }

            override fun onFailure(message: String) {
                mView?.hideLoading()
                mView?.showNetworkAlert()
            }
        })
    }
    override fun onStatisticItemClick(parentKey: String, childKey: String) {
        mView?.openDetail(parentKey,childKey)
    }

    override fun attachView(view: StatisticContract.View) {
        mView = view
        view.attachPresenter(this)
    }

    override fun detachView() {
        mView = null
    }

    override fun checkNetwork(context: Context) {
        if (InternetUtil.checkInternet(context)){
            mView?.showNetworkAlert()
        }
    }
}