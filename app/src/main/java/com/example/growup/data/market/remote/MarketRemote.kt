package com.example.growup.data.market.remote

import com.example.core.firebase.FirebaseClient
import com.example.growup.data.market.MarketDataSource
import com.example.growup.data.market.model.Products
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MarketRemote : FirebaseClient() , MarketDataSource {
    companion object{
        private var INSTANCE: MarketRemote? = null
        fun getInstance(): MarketRemote {
            if(INSTANCE == null){
                INSTANCE =
                    MarketRemote()
            }
            return INSTANCE!!
        }
    }
    private var marketSales = getRef(MarketRemoteContains.MARKET_REF).child(MarketRemoteContains.MARKER_SALE)
    override fun getMarketData(callback: MarketDataSource.RequestCallback){
        val products = HashMap<String, Products>()
        marketSales.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError){
                callback.onFailure(p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    products[it.key.toString()] = it.getValue(Products::class.java)!!
                }
                callback.onSuccess(products)
            }

        })
    }

    private var marketSold= getRef(MarketRemoteContains.MARKET_REF).child(MarketRemoteContains.MARKET_SOLD)
    override fun getMarketSold(callback: MarketDataSource.RequestCallback){
        val productsSold = HashMap<String, Products>()
        marketSold.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError){
                callback.onFailure(p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    productsSold[it.key.toString()] = it.getValue(Products::class.java)!!
                }
                callback.onSuccess(productsSold)
            }

        })
    }


}
