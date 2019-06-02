package com.growup.growup.ui.market

import android.content.Context
import com.entezeer.tracking.utils.InternetUtil
import com.growup.growup.data.market.MarketDataSource
import com.growup.growup.data.market.model.Products
class MarketPresenter(private val marketDataSource: MarketDataSource): MarketContract.Presenter {
    private var mView: MarketContract.View? = null
    private var data = HashMap<String, Products>()

    override fun getCurrentUserProducts(uid: String) {
        marketDataSource.getCurrentUserProducts(object: MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                result.forEach{
                    if(it.value.uid == uid.trim()){
                        data[it.key] = it.value
                    }
                }
                mView?.showData(data)
            }

            override fun onFailure(message: String) {
                mView?.showNetworkAlert()
            }

        })
    }

    override fun getMarketSold(uid: String) {

        marketDataSource.getMarketSold(object: MarketDataSource.RequestCallback {
            override fun onSuccess(result: HashMap<String, Products>) {
                result.forEach{
                    if(it.value.uid == uid.trim()){
                        data[it.key] = it.value
                    }
                }
                mView?.showData(data)
            }

            override fun onFailure(message: String) {
                mView?.showNetworkAlert()
            }
        })
    }

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


}