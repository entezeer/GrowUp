package com.example.growup.ui.statistic

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LineChartActivity : AppCompatActivity() {

    private var chartOne: LineChart? = null
    var axisData: Array<Int> = arrayOf(2014, 2015, 2016, 2017, 2018)
    var axisValues: ArrayList<Entry> = ArrayList()
    var axisValuesCow: ArrayList<Entry> = ArrayList()
    var mKey: String? = null
    var mChildKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)
        mKey = intent.getStringExtra(DetailStatisticActivity.EXTRA_KEY)
        mChildKey = intent.getStringExtra(DetailStatisticActivity.EXTRA_CHILDKEY)

        init()
        initData()
    }

    private fun init() {
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)


        chartOne = findViewById(R.id.chart)
        chartOne?.setTouchEnabled(true)
        chartOne?.setPinchZoom(true)

    }

    private fun initData() {
        GrowUpApplication.mAnimalStatisticRef.child(mKey!!).child(mChildKey!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var pointX = 0
                    dataSnapshot.children.forEach {
                        axisValues.add(Entry(pointX.toFloat(), it.value.toString().toFloat()))
                        pointX++
                        Toast.makeText(this@LineChartActivity, axisValues.toString(), Toast.LENGTH_LONG).show()
                    }
                    draw(axisValues)
                }
            })

    }

    private fun draw(values: ArrayList<Entry>) {
        val dataSet = LineDataSet(values, "$mKey, $mChildKey")
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)


        chartOne?.xAxis?.position = XAxis.XAxisPosition.TOP

        chartOne?.xAxis?.granularity = 1f
        chartOne?.xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                axisData.forEach {
                    return it.toString()
                }
                return ""
            }
        }

        val yAxisLeft = chartOne?.axisLeft
        yAxisLeft?.granularity = 1f

        chartOne?.legend?.form = Legend.LegendForm.CIRCLE

        val lineData = LineData(dataSet)
        chartOne?.axisRight?.isEnabled = false
        chartOne?.data = lineData
        chartOne?.animateX(1500)
        chartOne?.invalidate()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object{
        val EXTRA_KEY = "EXTRA_KEY"
        val EXTRA_CHILDKEY = "EXTRA_CHILDKEY"
        fun start(context: Activity, key: String, childKey: String) {
            val intent = Intent(context, LineChartActivity::class.java)
            intent.putExtra(EXTRA_KEY, key)
            intent.putExtra(EXTRA_CHILDKEY, childKey)
            ContextCompat.startActivity(context, intent, Bundle())
        }
    }
}
