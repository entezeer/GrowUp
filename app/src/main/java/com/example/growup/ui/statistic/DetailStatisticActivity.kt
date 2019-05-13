package com.example.growup.ui.statistic

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.PercentFormatter


class DetailStatisticActivity : AppCompatActivity() {

    private var pieChart: PieChart? = null
    private var chartData: ArrayList<PieEntry> = ArrayList()
    private var chartDataName: ArrayList<String> = ArrayList()
    var mKey: String? = null
    var mChildKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_statistic)
        mKey = intent.getStringExtra(EXTRA_KEY)
        mChildKey = intent.getStringExtra(EXTRA_CHILDKEY)

        init()

        initData()

        Toast.makeText(this@DetailStatisticActivity, mKey, Toast.LENGTH_LONG).show()
        Toast.makeText(this@DetailStatisticActivity, mChildKey, Toast.LENGTH_LONG).show()
    }

    private fun init() {
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        pieChart = findViewById(R.id.piechart)
    }


    private fun initData() {
        GrowUpApplication.mStatisticRef.child(mKey!!).child(mChildKey!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        var index = 0f
                        chartData.add(PieEntry(it.getValue(String::class.java)!!.toFloat(), index))
                        chartDataName.add(it.key.toString())
                        index++
                    }
                    val dataSet = PieDataSet(chartData, mKey)
                    val pieData = PieData(dataSet)

                    pieData.setValueFormatter(PercentFormatter(pieChart))
                    val colors: MutableList<Int> =
                        arrayOf(Color.GRAY, Color.YELLOW, Color.RED, Color.GREEN, Color.CYAN).toMutableList()

                    dataSet.colors = colors

                    pieChart?.legend?.form = Legend.LegendForm.CIRCLE

                    pieChart?.data = pieData
                    pieChart?.invalidate()
                    pieChart?.animateXY(2000, 2000)
                }

            })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object {
        val EXTRA_KEY = "EXTRA_KEY"
        val EXTRA_CHILDKEY = "EXTRA_CHILDKEY"
        fun start(context: Activity, key: String, childKey: String) {
            val intent = Intent(context, DetailStatisticActivity::class.java)
            intent.putExtra(EXTRA_KEY, key)
            intent.putExtra(EXTRA_CHILDKEY, childKey)
            startActivity(context, intent, Bundle())
        }
    }
}
