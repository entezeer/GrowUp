package com.example.growup.data

import com.example.growup.data.statistic.StatisticDataSource
import com.example.growup.data.statistic.StatisticRepository
import com.example.growup.data.statistic.remote.StatisticRemote
import com.example.growup.data.user.UserDataSource
import com.example.growup.data.user.UserRepository
import com.example.growup.data.user.remote.UserRemote

object RepositoryProvider {
    fun getStatisticDataSource():StatisticDataSource = StatisticRepository.getInstance(StatisticRemote.getInstance())

    fun getUserDataSource():UserDataSource = UserRepository.getInstance(UserRemote.getInstance())
}