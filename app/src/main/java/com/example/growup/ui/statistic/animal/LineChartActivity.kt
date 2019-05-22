package com.example.growup.ui.statistic.animal

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.ui.statistic.PieChartActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import javax.xml.datatype.DatatypeConstants.HOURS



class LineChartActivity : AppCompatActivity() {

    private var chartOne: LineChart? = null
    var axisData = arrayOf("2014", "2015", "2016", "2017", "2018")
    var axisValues: ArrayList<Entry> = ArrayList()
    var axisValuesCow: ArrayList<Entry> = ArrayList()
    var mKey: String? = null
    var mChildKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)
        mKey = intent.getStringExtra(PieChartActivity.EXTRA_KEY)
        mChildKey = intent.getStringExtra(PieChartActivity.EXTRA_CHILDKEY)

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
                    }
                    draw(axisValues)
                }
            })

    }

    private fun draw(values: ArrayList<Entry>) {
        val dataSet = LineDataSet(values, "$mKey, $mChildKey")
        val formatter = DecimalFormat()
        val largeValue = LargeValueFormatter()
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.lineWidth = 3f
        dataSet.setDrawFilled(true)
        dataSet.valueTextSize = 10f
        dataSet.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
            formatter.format(value) + " Голов"
        }

        val xAxis = chartOne?.xAxis
        xAxis?.position = XAxis.XAxisPosition.TOP

        xAxis?.granularity = 1f
        xAxis?.setDrawLabels(true)
        xAxis?.valueFormatter
        xAxis?.valueFormatter = IAxisValueFormatter { value, axis -> axisData[value.toInt()] }

        val yAxisLeft = chartOne?.axisLeft
        yAxisLeft?.granularity = 1f
        yAxisLeft?.valueFormatter = largeValue
        chartOne?.legend?.form = Legend.LegendForm.CIRCLE

        chartOne?.axisLeft?.setDrawGridLines(false)
        chartOne?.xAxis?.setDrawGridLines(false)

        val lineData = LineData(dataSet)
        chartOne?.description?.isEnabled = false
        chartOne?.axisRight?.isEnabled = false
        chartOne?.data = lineData
        chartOne?.animateX(1000)
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
