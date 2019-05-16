package com.example.growup.ui.statistic


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.entezeer.tracking.utils.InternetUtil
import com.example.core.extensions.slideRightOut
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.statistic.model.ParentList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StatisticFragment : Fragment(), StatisticContract.View, ExpandableRecyclerAdapter.Listener {

    private var animalStatistic: CardView? = null
    private var mPresenter: StatisticContract.Presenter? = null
    private var statisticRecycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_statistic, container, false)

        mPresenter = StatisticPresenter(RepositoryProvider.getStatisticDataSource())
        mPresenter?.attachView(this)

        init(rootView)

        mPresenter?.getData()

        return rootView
    }

    fun init(view: View){
        statisticRecycler = view.findViewById(R.id.statistic_recycler)
        statisticRecycler?.layoutManager = LinearLayoutManager(activity)
        statisticRecycler?.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        progressBar = view.findViewById(R.id.progress_bar)

        animalStatistic = view.findViewById(R.id.animal_statistic)
        animalStatistic?.setOnClickListener {
            startActivity(Intent(activity,AnimalStatisticActivity::class.java))
        }
    }

    override fun showLoading() {
        animalStatistic?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        animalStatistic?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    override fun showNetworkAlert() {
        activity?.let { InternetUtil.showInternetAlert(it) }
    }

    override fun showData(data: ArrayList<ParentList>) {
        statisticRecycler?.adapter = activity?.let { ExpandableRecyclerAdapter(data, it,this) }
    }

    override fun openDetail(parentKey: String, childKey: String) {
        activity?.let { DetailStatisticActivity.start(it,parentKey,childKey) }
    }

    override fun finishView() {
        activity?.finish()
        activity?.slideRightOut()
    }

    override fun attachPresenter(presenter: StatisticContract.Presenter) {
        mPresenter = presenter
    }

    override fun onItemSelectedAt(key: String, childKey: String?) {
        childKey?.let { mPresenter?.onStatisticItemClick(key, it) }
    }

    companion object {
        fun newInstance():StatisticFragment = StatisticFragment()
    }
}
