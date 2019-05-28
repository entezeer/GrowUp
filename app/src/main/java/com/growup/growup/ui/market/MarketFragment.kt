package com.growup.growup.ui.market

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import com.entezeer.tracking.utils.InternetUtil
import com.growup.core.extensions.slideRightOut
import com.growup.core.firebase.FirebaseClient
import com.growup.growup.GrowUpApplication
import com.growup.growup.R
import com.growup.growup.data.RepositoryProvider
import com.growup.growup.data.market.model.Products
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.model.User
import com.growup.growup.ui.detail.DetailDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MarketFragment : Fragment(), MarketRecyclerAdapter.Listener, MarketContract.View {
    private var mNoData : TextView? = null
    private var mMarketPresenter: MarketContract.Presenter? = null
    private var mData: ArrayList<Products> = ArrayList()
    private var mMarketRecyclerView: RecyclerView? = null
    private var mAddButton: FloatingActionButton? = null
    private var adapter: MarketRecyclerAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mDataKeys: ArrayList<String> = ArrayList()

    companion object {
        fun newInstance(): MarketFragment = MarketFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_market, container, false)
        init(view)
        mMarketPresenter = MarketPresenter(RepositoryProvider.getMarketDataSource())
        mMarketPresenter?.attachView(this)
        mMarketPresenter?.getMarketData()

        return view
    }

    private fun init(view: View) {
        mNoData = view.findViewById(R.id.no_data)
        mNoData?.visibility = View.GONE

        mMarketRecyclerView = view.findViewById(R.id.market_recycler)
        mMarketRecyclerView?.layoutManager = GridLayoutManager(activity, 2)

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            mMarketPresenter?.getMarketData()
        }
        mProgressBar = view.findViewById(R.id.progress_bar)

        mAddButton = view.findViewById(R.id.add_announcement)

        RepositoryProvider.getUserDataSource()
            .getUser(FirebaseClient().getAuth().currentUser?.uid!!, object : UserDataSource.UserCallback {
                override fun onSuccess(result: User) {
                    if (result.userType == "Оптовик") {
                        mAddButton?.hide()
                    }
                }

                override fun onFailure(message: String) {
                }

            })
        mAddButton?.setOnClickListener {
            startActivity(Intent(activity, AddAnnouncementActivity::class.java))
        }
    }

    private fun updateUi() {
        adapter = activity?.let { MarketRecyclerAdapter(mData, this, it) }

        if (mData.isEmpty()){
            mNoData?.visibility = View.VISIBLE
        }

        mProgressBar?.visibility = View.GONE
        mMarketRecyclerView?.adapter = adapter
        mSwipeRefreshLayout?.isRefreshing = false
    }


    override fun onItemSelectedAt(position: Int) {
        GrowUpApplication.productsData = mData
        val detailDialogFragment = DetailDialogFragment.newInstance(mDataKeys[position], "Market", mData[position])
        detailDialogFragment.show(fragmentManager, "detailDialogFragment")
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
        mMarketRecyclerView?.adapter = activity?.let { MarketRecyclerAdapter(mData, this, it) }
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
        mMarketPresenter = presenter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchItem.setOnMenuItemClickListener {
            searchView.setIconifiedByDefault(true)
            searchView.isFocusable = true
            searchView.isIconified = false
            searchView.requestFocusFromTouch()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }
}