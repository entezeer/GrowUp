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
import com.entezeer.tracking.utils.InternetUtil
import com.example.core.extensions.slideRightOut

import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.ui.market.MarketPresenter
import com.example.growup.data.market.model.Products
import com.example.growup.ui.detail.DetailDialogFragment
import com.example.growup.ui.market.MarketContract
import com.example.growup.ui.market.MarketRecyclerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OnSaleFragment : Fragment(), MarketRecyclerAdapter.Listener , MarketContract.View{

    private var mOnSalesPresenter: MarketContract.Presenter? = null
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
        mOnSalesPresenter= MarketPresenter(RepositoryProvider.getMarketDataSource())
        mOnSalesPresenter?.attachView(this)
        mOnSalesPresenter?.getMarketData()
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

    override fun showNetworkAlert() {
        activity?.let { InternetUtil.showInternetAlert(it) }
    }

    override fun showAlert() {

    }

    override fun showData(data: HashMap<String, Products>) {
        val uid = arguments?.getString(ARG_UID)
        for(entry: Map.Entry<String, Products> in data.entries ){
            if (entry.value.uid == uid){
                mData.add(entry.value)
                mDataKeys.add(entry.key)
            }
        }
        recyclerView?.adapter = activity?.let { MarketRecyclerAdapter(mData,this, it) }
        mProgressBar?.visibility = View.GONE
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
        mOnSalesPresenter = presenter
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
