package com.example.growup.data.statistic

import com.example.core.callback.BaseCallback
import com.example.growup.data.statistic.model.ParentList

interface StatisticDataSource {
    fun getStatistic(refKey: String,callback: RequestCallback)

    fun setStatistic()

    interface RequestCallback: BaseCallback<ArrayList<ParentList>>
}