package com.growup.growup.ui.market.sale


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.entezeer.tracking.utils.InternetUtil
import com.growup.core.extensions.slideRightOut

import com.growup.growup.R
import com.growup.growup.data.RepositoryProvider
import com.growup.growup.data.market.model.Products
import com.growup.growup.ui.detail.DetailDialogFragment
import com.growup.growup.ui.market.MarketContract
import com.growup.growup.ui.market.MarketPresenter
import com.growup.growup.ui.market.MarketRecyclerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyOrderFragment : Fragment(), MarketRecyclerAdapter.Listener, MarketContract.View {

    private var mNoData: TextView? = null
    private var mOrderPresenter: MarketContract.Presenter? = null
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
        val view = inflater.inflate(R.layout.fragment_my_order, container, false)
        init(view)
        mOrderPresenter = MarketPresenter(RepositoryProvider.getMarketDataSource())
        mOrderPresenter?.attachView(this)
        mOrderPresenter?.getCurrentUserOrders(arguments?.getString(ARG_UID)!!)
        return view
    }

    private fun init(view: View) {
        mNoData = view.findViewById(R.id.no_data)
        mNoData?.visibility = View.GONE

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = GridLayoutManager(activity, 2)

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            mOrderPresenter?.getCurrentUserOrders(arguments?.getString(ARG_UID)!!)
        }
        mProgressBar = view.findViewById(R.id.progress_bar)
    }

    override fun showNetworkAlert() {
        activity?.let { InternetUtil.showInternetAlert(it) }
    }

    override fun showAlert() {

    }

    override fun showData(data: HashMap<String, Products>) {
        mData.removeAll(mData)
        mDataKeys.removeAll(mDataKeys)

        mData.addAll(data.values)
        mDataKeys.addAll(data.keys)
        updateUi()
    }

    override fun openDetail(data: HashMap<String, Products>) {
        mDataKeys.addAll(data.keys)
    }

    override fun finishView() {
        activity?.finish()
        activity?.slideRightOut()
    }

    override fun attachPresenter(presenter: MarketContract.Presenter) {
        mOrderPresenter = presenter
    }

    private fun updateUi() {
        if (mData.isEmpty()) {
            mNoData?.visibility = View.VISIBLE
        }
        adapter = activity?.let { MarketRecyclerAdapter(mData, this, it) }
        mProgressBar?.visibility = View.GONE
        recyclerView?.adapter = adapter
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun onItemSelectedAt(position: Int) {
        val detailDialogFragment =
            DetailDialogFragment.newInstance(mDataKeys[position], "MyOrderFragment", mData[position])
        detailDialogFragment.show(fragmentManager, "detailDialogFragment")
    }

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_UID = "uid"
        const val TITLE = "Заявки"
        fun newInstance(position: Int, uid: String): Fragment {
            val fragment = MyOrderFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            bundle.putString(ARG_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }

}