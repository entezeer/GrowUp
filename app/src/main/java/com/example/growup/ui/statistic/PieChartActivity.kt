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
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.utils.ColorTemplate


class PieChartActivity : AppCompatActivity() {

    private var pieChart: PieChart? = null
    private var chartData: ArrayList<PieEntry> = ArrayList()
    private var chartDataName: ArrayList<LegendEntry> = ArrayList()
    var mKey: String? = null
    var mChildKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)
        mKey = intent.getStringExtra(EXTRA_KEY)
        mChildKey = intent.getStringExtra(EXTRA_CHILDKEY)

        init()

        initData()
    }

    private fun init() {
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        pieChart = findViewById(R.id.piechart)
    }


    private fun initData() {
        val colors: MutableList<Int> =
            arrayOf(Color.GRAY, Color.YELLOW, Color.RED, Color.GREEN, Color.CYAN).toMutableList()

        GrowUpApplication.mStatisticRef.child(mKey!!).child(mChildKey!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var index = 0f
                    dataSnapshot.children.forEach {
                        chartData.add(PieEntry(it.value.toString().toFloat(), index))
                        chartDataName.add(
                            LegendEntry
                                (
                                it.key.toString(),
                                Legend.LegendForm.CIRCLE,
                                20f,
                                20f,
                                null,
                                colors[index.toInt()]
                            )
                        )
                        index++
                    }
                    val dataSet = PieDataSet(chartData, "")
                    val pieData = PieData(dataSet)


                    dataSet.colors = colors

                    pieData.setValueTextColor(Color.BLACK)
                    dataSet.valueLinePart1Length = .3f
                    dataSet.valueLinePart2Length = .2f
                    dataSet.valueTextColor = Color.BLACK
                    dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    dataSet.sliceSpace = 3f
                    dataSet.selectionShift = 5f
                    pieChart?.centerText = mChildKey

                    pieChart?.legend?.form = Legend.LegendForm.CIRCLE
                    pieChart?.legend?.setCustom(chartDataName)

                    pieChart?.description?.text = mKey
                    pieChart?.data = pieData
                    pieChart?.invalidate()
                    pieChart?.animateXY(1000, 1000)
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
            val intent = Intent(context, PieChartActivity::class.java)
            intent.putExtra(EXTRA_KEY, key)
            intent.putExtra(EXTRA_CHILDKEY, childKey)
            startActivity(context, intent, Bundle())
        }
    }
}
