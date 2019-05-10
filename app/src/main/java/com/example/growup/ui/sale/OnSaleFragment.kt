package com.example.growup.ui.sale


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.growup.GrowUpApplication

import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.ui.detail.DetailDialogFragment
import com.example.growup.ui.market.MarketRecyclerAdapter
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
class OnSaleFragment : Fragment(), MarketRecyclerAdapter.Listener {

    private var mData: ArrayList<Products> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var adapter: MarketRecyclerAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mDataKeys: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_sale, container, false)

        init(view)
        initData()
        return view
    }

    private fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = GridLayoutManager(activity, 2)

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            updateUi()
        }
//        mProgressBar = view.findViewById(R.id.progress_bar)
    }

    private fun initData() {
        val uid = arguments?.getString(ARG_UID)
        GrowUpApplication.mMarketRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, databaseError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    if (it.getValue(Products::class.java)!!.uid == uid) {
                        mData.add(it.getValue(Products::class.java)!!)
                        mDataKeys.add(it.key.toString())
                    }
                }
                updateUi()
            }
        })
    }

    private fun updateUi(){
        adapter = activity?.let { MarketRecyclerAdapter(mData, this, it) }
//        mProgressBar?.visibility = View.GONE
        recyclerView?.adapter = adapter
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun onItemSelectedAt(position: Int) {
        val detailDialogFragment = DetailDialogFragment.newInstance(mDataKeys[position] , "OnSalesFragment", mData[position])
        detailDialogFragment.show(fragmentManager, "detailDialogFragment")
    }

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_UID = "uid"
        const val TITLE = "Продажа"
        fun newInstance(position: Int, uid: String): Fragment {
            val fragment = OnSaleFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            bundle.putString(ARG_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }
}
