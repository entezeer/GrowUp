package com.growup.growup.ui.statistic.animal

import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.growup.growup.data.statistic.StatisticDataSource
import com.growup.growup.data.statistic.model.ParentList
import com.growup.growup.data.statistic.remote.StatisticRemoteConstants

class AnimalStatisticPresenter(private val mStatisticDataSource: StatisticDataSource): AnimalStatisticContract.Presenter {


    private var mView: AnimalStatisticContract.View? = null

    override fun getData() {
        mView?.showLoading()
        mStatisticDataSource.getStatistic(StatisticRemoteConstants.ANIMAL_KEY,object : StatisticDataSource.RequestCallback{
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

    override fun attachView(view: AnimalStatisticContract.View) {
        mView = view
        view.attachPresenter(this)
    }

    override fun detachView() {
        mView = null
    }

    override fun checkNetwork(context: Context) {
        if (!InternetUtil.checkInternet(context)){
            mView?.showNetworkAlert()
        }
    }
}