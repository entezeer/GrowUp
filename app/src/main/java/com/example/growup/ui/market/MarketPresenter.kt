package com.example.growup.ui.market


import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.data.market.MarketDataSource
import com.example.growup.data.market.model.Products

class MarketPresenter(private val marketDataSource: MarketDataSource): MarketContract.Presenter {
    override fun getMarketSold() {
        marketDataSource.getMarketSold(object: MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                mView?.showData(result)
            }

            override fun onFailure(message: String) {
                mView?.showNetworkAlert()
            }

        })
    }

    override fun checkNetwork(context: Context) {
        if (InternetUtil.checkInternet(context)){
            mView?.showNetworkAlert()
        }
    }

    override fun onProductClick(data: HashMap<String, Products>) {
        mView?.openDetail(data)
    }

    override fun attachView(view: MarketContract.View) {
        mView = view
        mView?.attachPresenter(this)
    }

    override fun detachView() {
        mView = null
    }

    private var mView: MarketContract.View? = null
    override fun getMarketData() {
        marketDataSource.getMarketData(object: MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                mView?.showData(result)
            }

            override fun onFailure(message: String) {
                mView?.showNetworkAlert()
            }

        })
    }


}