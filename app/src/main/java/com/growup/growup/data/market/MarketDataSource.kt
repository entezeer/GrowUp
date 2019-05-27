package com.growup.growup.data.market

import com.growup.core.callback.BaseCallback
import com.growup.growup.data.market.model.Products

interface MarketDataSource {
    fun getMarketData(callback: RequestCallback)
    fun getMarketSold(callback: RequestCallback)
    fun getCurrentUserProducts(callback: RequestCallback)
    fun setMarketData(product: Products, callback: SuccessCallback)
    fun setMarketSold(product: Products, callback: SuccessCallback)


    interface RequestCallback : BaseCallback<HashMap<String, Products>>

    interface SuccessCallback : BaseCallback<String>
}