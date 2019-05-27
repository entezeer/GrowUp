package com.growup.growup.data.statistic

import com.growup.growup.data.statistic.model.ParentList
import com.growup.growup.data.statistic.remote.StatisticRemote

class StatisticRepository(private val remoteSource: StatisticRemote) : StatisticDataSource {

    companion object {

        private var INSTANCE: StatisticDataSource? = null
        fun getInstance(remoteSource: StatisticRemote): StatisticDataSource {
            if (INSTANCE == null) {
                INSTANCE = StatisticRepository(remoteSource)
            }
            return INSTANCE!!
        }
    }


    override fun getStatistic(refKey: String, callback: StatisticDataSource.RequestCallback) {
        remoteSource.getStatistic(refKey ,object : StatisticDataSource.RequestCallback{
            override fun onSuccess(result: ArrayList<ParentList>) {
                callback.onSuccess(result)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

        })
    }

    override fun setStatistic() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}