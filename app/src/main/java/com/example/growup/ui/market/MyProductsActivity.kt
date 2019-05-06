package com.example.growup.ui.market

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.ui.detail.DetailDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyProductsActivity : AppCompatActivity(), MarketRecyclerAdapter.Listener {

    private var mData: ArrayList<Products> = ArrayList()
    private var marketRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        init()
        initData()
    }

    private fun init() {
        marketRecyclerView = findViewById(R.id.market_recycler)
        marketRecyclerView?.layoutManager = GridLayoutManager(this, 2)

    }

    private fun initData(){
        GrowUpApplication.mMarketRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MyProductsActivity,databaseError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    if (it.getValue(Products::class.java)!!.uid == GrowUpApplication.mAuth.currentUser?.uid){
                        mData.add(it.getValue(Products::class.java)!!)
                    }
                }
                updateUi()
            }
        })
    }

    private fun updateUi(){
        marketRecyclerView?.adapter = MarketRecyclerAdapter(mData,this,this)
    }

    @SuppressLint("CommitTransaction")
    override fun onItemSelectedAt(position: Int) {
        val detailDialogFragment = DetailDialogFragment.newInstance(position)
        detailDialogFragment.show(supportFragmentManager.beginTransaction(),"detailDialogFragment")
    }
}
