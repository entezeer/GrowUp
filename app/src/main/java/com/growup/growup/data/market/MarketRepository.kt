package com.growup.growup.data.market

import com.growup.growup.data.market.model.Products
import com.growup.growup.data.market.remote.MarketRemote

class MarketRepository(private val remoteSource: MarketRemote) : MarketDataSource{

    companion object{

        private var INSTANCE: MarketDataSource? = null

        fun getInstance(remoteSource: MarketRemote):MarketDataSource{
            if (INSTANCE == null){
                INSTANCE = MarketRepository(remoteSource)
            }
            return INSTANCE!!
        }
    }
    override fun getMarketSold(callback: MarketDataSource.RequestCallback) {
        remoteSource.getMarketSold(object : MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                callback.onSuccess(result)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

        })
    }
    override fun getMarketData(callback: MarketDataSource.RequestCallback) {
        remoteSource.getMarketData(object:MarketDataSource.RequestCallback{
            override fun onSuccess(result: HashMap<String, Products>) {
                callback.onSuccess(result)
            }
            override fun onFailure(message: String) {
               callback.onFailure(message)
            }

        })
    }

    override fun getCurrentUserProducts(callback: MarketDataSource.RequestCallback) {
        remoteSource.getCurrentUserProducts(object : MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                callback.onSuccess(result)
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }
        })
    }

    override fun setMarketData(product: Products, callback: MarketDataSource.SuccessCallback) {
        remoteSource.setMarketData(product, object : MarketDataSource.SuccessCallback{
            override fun onSuccess(result: String) {
                callback.onSuccess("success")
            }

            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

        })
    }

    override fun setMarketSold(product: Products, callback: MarketDataSource.SuccessCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMarketData(productKey: String, callback: MarketDataSource.SuccessCallback) {
        remoteSource.removeMarketData(productKey, object : MarketDataSource.SuccessCallback{
            override fun onFailure(message: String) {
                callback.onFailure(message)
            }

            override fun onSuccess(result: String) {
                callback.onSuccess(result)
            }

        })
    }

}
