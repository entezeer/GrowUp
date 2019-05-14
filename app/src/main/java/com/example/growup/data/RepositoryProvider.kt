package com.example.growup.data

import com.example.growup.data.statistic.StatisticDataSource
import com.example.growup.data.statistic.StatisticRepository
import com.example.growup.data.statistic.remote.StatisticRemote

object RepositoryProvider {
    fun getStatisticDataSource():StatisticDataSource = StatisticRepository.getInstance(StatisticRemote.getInstance())
}