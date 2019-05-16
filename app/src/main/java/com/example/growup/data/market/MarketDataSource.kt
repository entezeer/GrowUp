package com.example.growup.data.market

import com.example.core.callback.BaseCallback
import com.example.growup.data.market.model.Products

interface MarketDataSource {
    fun getMarketData(callback: RequestCallback)
    fun getMarketSold(callback: RequestCallback)


    interface RequestCallback: BaseCallback<HashMap<String, Products>>
}