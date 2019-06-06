package com.growup.growup.data.market

import com.growup.core.callback.BaseCallback
import com.growup.growup.data.market.model.Products

interface MarketDataSource {
    fun getMarketData(callback: RequestCallback)
    fun getMarketSold(callback: RequestCallback)
    fun getCurrentUserProducts(callback: RequestCallback)
    fun setMarketData(product: Products, callback: SuccessCallback)
    fun setMarketSold(product: Products, callback: SuccessCallback)
    fun removeMarketData(productKey: String, callback: SuccessCallback)

    fun getOrderData(callback: RequestCallback)
    fun setOrderData(product: Products, callback: SuccessCallback)
    fun removeOrderData(productKey: String, callback: SuccessCallback)
    fun getCurrentUserOrders(callback: RequestCallback)

    interface RequestCallback : BaseCallback<HashMap<String, Products>>

    interface SuccessCallback : BaseCallback<String>
}