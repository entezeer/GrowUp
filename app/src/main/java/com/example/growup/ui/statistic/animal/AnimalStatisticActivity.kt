package com.example.growup.ui.statistic.animal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.entezeer.tracking.utils.InternetUtil
import com.example.core.extensions.slideRightOut
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.statistic.model.ChildList
import com.example.growup.data.statistic.model.ParentList
import com.example.growup.ui.statistic.ExpandableRecyclerAdapter
import com.example.growup.ui.statistic.PieChartActivity
import com.example.growup.ui.statistic.StatisticContract
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AnimalStatisticActivity : AppCompatActivity(), AnimalStatisticContract.View, ExpandableRecyclerAdapter.Listener {


    private var statisticRecycler: RecyclerView? = null
    private var mPresenter: AnimalStatisticContract.Presenter? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_statistic)

        mPresenter = AnimalStatisticPresenter(RepositoryProvider.getStatisticDataSource())
        mPresenter?.attachView(this)
        mPresenter?.checkNetwork(this)


        init()

        mPresenter?.getData()

    }

    private fun init() {
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        statisticRecycler = findViewById(R.id.statistic_recycler)
        statisticRecycler?.layoutManager = LinearLayoutManager(this)
        statisticRecycler?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun showNetworkAlert() {
        InternetUtil.showInternetAlert(this)
    }

    override fun showData(data: ArrayList<ParentList>) {
        statisticRecycler?.adapter = ExpandableRecyclerAdapter(data, this,this)
    }

    override fun openDetail(parentKey: String, childKey: String) {
        LineChartActivity.start(this,parentKey,childKey)
    }

    override fun finishView() {
        finish()
        slideRightOut()
    }

    override fun attachPresenter(presenter: AnimalStatisticContract.Presenter) {
        mPresenter = presenter
    }

    override fun onItemSelectedAt(key: String, childKey: String?) {
        childKey?.let { mPresenter?.onStatisticItemClick(key, it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
