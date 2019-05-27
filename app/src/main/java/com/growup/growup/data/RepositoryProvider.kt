package com.growup.growup.data

import com.growup.growup.data.market.MarketDataSource
import com.growup.growup.data.market.remote.MarketRemote
import com.growup.growup.data.market.MarketRepository
import com.growup.growup.data.statistic.StatisticDataSource
import com.growup.growup.data.statistic.StatisticRepository
import com.growup.growup.data.statistic.remote.StatisticRemote
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.UserRepository
import com.growup.growup.data.user.remote.UserRemote

object RepositoryProvider {
    fun getStatisticDataSource():StatisticDataSource = StatisticRepository.getInstance(StatisticRemote.getInstance())
    fun getMarketDataSource():MarketDataSource = MarketRepository.getInstance(MarketRemote.getInstance())
    fun getUserDataSource():UserDataSource = UserRepository.getInstance(UserRemote.getInstance())
}