package com.example.growup.ui.market

import android.content.Context
import com.example.core.mvp.BaseContract
import com.example.growup.data.market.model.Products

interface MarketContract {
    interface View: BaseContract.View<Presenter>{
        fun showNetworkAlert()
        fun showAlert()
        fun showData(data: HashMap<String, Products>)
        fun openDetail(data: HashMap<String, Products>)
    }

    interface Presenter: BaseContract.Presenter<View>{
        fun getMarketData()
        fun getMarketSold()
        fun checkNetwork(context: Context)
        fun onProductClick(data: HashMap<String, Products>)
    }
}