package com.example.growup.ui.statistic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AnimalStatisticActivity : AppCompatActivity() {

    private var chartOne: LineChart? = null
    var axisData = arrayOf(2014, 2015, 2016, 2017, 2018)
    var axisValues: ArrayList<Entry> = ArrayList()
    var axisValuesCow: ArrayList<Entry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_statistic)

        init()

        initData()

//        draw()
    }
    private fun init(){
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)


        chartOne = findViewById(R.id.chart)
        chartOne?.setTouchEnabled(true)
        chartOne?.setPinchZoom(true)

    }
    private fun initData(){
        GrowUpApplication.mAnimalStatisticRef.child("Иссык-Кульская область").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var pointX = 0
                dataSnapshot.child("Домашняя птица").children.forEach {
                    axisValues.add(Entry(axisData[pointX].toString().toFloat(),it.value.toString().toFloat()))
                    pointX++
                    Toast.makeText(this@AnimalStatisticActivity,axisValues.toString(),Toast.LENGTH_LONG).show()
                }
                draw(axisValues)
            }
        })

        GrowUpApplication.mAnimalStatisticRef.child("Иссык-Кульская область").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var pointX = 0
                dataSnapshot.child("Коровы").children.forEach {
                    axisValuesCow.add(Entry(pointX.toFloat(),it.value.toString().toFloat()))
                    pointX++
                    Toast.makeText(this@AnimalStatisticActivity,axisValuesCow.toString(),Toast.LENGTH_LONG).show()
                }
            }

        })
    }
    private fun draw(values: ArrayList<Entry>){
        val dataSet = LineDataSet(values,"Иссык-Кульская область, Домашняя птица")
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.valueTextColor = ContextCompat.getColor(this,R.color.black)

        val xAxis = chartOne?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return axisData[value.toInt()].toString()
            }
        }

        xAxis?.granularity = 1f
        xAxis?.valueFormatter = formatter

        val yAxisRight = chartOne?.axisRight
        yAxisRight?.granularity = 1f

        val yAxisLeft = chartOne?.axisLeft
        yAxisLeft?.granularity = 1f

        val lineData = LineData(dataSet)
        chartOne?.data = lineData
        chartOne?.animateX(2500)
        chartOne?.invalidate()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
