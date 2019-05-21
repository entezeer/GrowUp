package com.example.growup.ui.market

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.market.MarketDataSource
import com.example.growup.data.market.model.Products
import com.example.growup.ui.detail.DetailDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FavoritesActivity : AppCompatActivity(), MarketRecyclerAdapter.Listener {

    private var mNoData: TextView? = null
    private var mData: ArrayList<Products> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var adapter: MarketRecyclerAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mDataKeys: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        init()

        initData()
    }

    private fun init() {
        supportActionBar?.title = "Избранное"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        mNoData = findViewById(R.id.no_data)
        mNoData?.visibility = View.GONE

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = GridLayoutManager(this, 2)

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            initData()
        }
    }

    private fun initData() {
        mDataKeys.removeAll(mDataKeys)
        mData.removeAll(mData)
        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).child("favorites")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@FavoritesActivity, databaseError.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        it.key?.let { it1 -> mDataKeys.add(it1) }
                    }

                    RepositoryProvider.getMarketDataSource().getMarketData(object : MarketDataSource.RequestCallback {
                        override fun onSuccess(result: HashMap<String, Products>) {

                            for (i in mDataKeys) {
                                if (result.containsKey(i)) {
                                    result[i]?.let { mData.add(it) }
                                }
                            }
                            updateUi()
                        }

                        override fun onFailure(message: String) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })

                }
            })


    }

    private fun updateUi() {
        if (mData.isEmpty()) {
            mNoData?.visibility = View.VISIBLE
        }
        adapter = MarketRecyclerAdapter(mData, this, this)
//        mProgressBar?.visibility = View.GONE
        recyclerView?.adapter = adapter
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onItemSelectedAt(position: Int) {
        val detailDialogFragment =
            DetailDialogFragment.newInstance(mDataKeys[position], "OnSalesFragment", mData[position])
        detailDialogFragment.show(supportFragmentManager, "detailDialogFragment")
    }

}
