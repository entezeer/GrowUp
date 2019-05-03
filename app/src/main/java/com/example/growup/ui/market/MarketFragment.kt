package com.example.growup.ui.market


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.ui.detail.DetailDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MarketFragment : Fragment(), MarketRecyclerAdapter.Listener {

    private var mData: ArrayList<Products> = ArrayList()
    private var mMarketRecyclerView: RecyclerView? = null
    private var mAddButton: FloatingActionButton? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mProgressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_market, container, false)

        init(view)

        initData()

        return view
    }

    private fun init(view: View){
        mMarketRecyclerView = view.findViewById(R.id.market_recycler)
        mMarketRecyclerView?.layoutManager = GridLayoutManager(activity, 2)

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            updateUi()
        }
        mProgressBar = view.findViewById(R.id.progress_bar)

        mAddButton = view.findViewById(R.id.add_announcement)
        mAddButton?.setOnClickListener {
            startActivity(Intent(activity,AddAnnouncementActivity::class.java))
        }
    }
    private fun initData(){
        GrowUpApplication.mMarketRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity,databaseError.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    mData.add(it.getValue(Products::class.java)!!)
                }
                updateUi()
            }
        })
    }
    private fun updateUi(){
        checkNetwork()
        mProgressBar?.visibility = View.GONE
        mMarketRecyclerView?.adapter = activity?.let { MarketRecyclerAdapter(mData,this, it) }
        mSwipeRefreshLayout?.isRefreshing = false
    }

    private fun checkNetwork(){
        if (!InternetUtil.checkInternet(activity!!)){
            Utils.showInternetAlert(activity!!)
        }
    }

    override fun onItemSelectedAt(position: Int) {
        GrowUpApplication.productsData = mData
        val detailDialogFragment = DetailDialogFragment.newInstance(position)
        detailDialogFragment.show(fragmentManager,"detailDialogFragment")
    }
}
