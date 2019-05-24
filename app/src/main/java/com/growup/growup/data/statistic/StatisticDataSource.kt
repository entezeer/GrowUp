package com.growup.growup.data.statistic

import com.growup.core.callback.BaseCallback
import com.growup.growup.data.statistic.model.ParentList

interface StatisticDataSource {
    fun getStatistic(refKey: String,callback: RequestCallback)

    fun setStatistic()

    interface RequestCallback: BaseCallback<ArrayList<ParentList>>
}