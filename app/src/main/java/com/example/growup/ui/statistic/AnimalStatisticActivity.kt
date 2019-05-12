package com.example.growup.ui.statistic

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import lecho.lib.hellocharts.view.LineChartView
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Viewport


class AnimalStatisticActivity : AppCompatActivity() {

    private var chartOne: LineChartView? = null
    var axisData = arrayOf("2014", "2015", "2016", "2017", "2018")
    var oneYAxisData = intArrayOf(0, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000)
    var yAxisValues: ArrayList<PointValue> = ArrayList()
    var axisValues: ArrayList<AxisValue> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_statistic)

        init()

        initData()

        draw()
    }
    private fun init(){
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)


        chartOne = findViewById(R.id.chart)

    }
    private fun initData(){
        GrowUpApplication.mAnimalStatisticRef.child("Иссык-Кульская область").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                var pointX = 0f
//                dataSnapshot.child("Домашняя птица").children.forEach {
//                    yAxisValues.add(PointValue(pointX,it.getValue(Float::class.java)!!))
//                    pointX++
//                    Toast.makeText(this@AnimalStatisticActivity,yAxisValues.toString(),Toast.LENGTH_LONG).show()
//                }

                yAxisValues.add(PointValue(0f,2f))
                yAxisValues.add(PointValue(1f,4f))
                yAxisValues.add(PointValue(2f,3f))
                yAxisValues.add(PointValue(3f,2f))
                yAxisValues.add(PointValue(4f,1f))
            }

        })
    }
    private fun draw(){

        for (i in 0 until axisData.size) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        val line = Line(yAxisValues).setColor(Color.GREEN)
        val lines: ArrayList<Line> = ArrayList()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines

        val axis = Axis()
        axis.values = axisValues
        axis.textSize = 16
        axis.textColor = Color.parseColor("#03A9F4")
        data.axisXBottom = axis

        val yAxis = Axis()
        yAxis.name = "Sales in millions";
        yAxis.textColor = Color.parseColor("#03A9F4");
        yAxis.textSize = 16;
        data.axisYLeft = yAxis

        chartOne?.lineChartData = data
        val viewport = Viewport(chartOne?.getMaximumViewport())
        viewport.top = 110f
        chartOne?.setMaximumViewport(viewport)
        chartOne?.setCurrentViewport(viewport)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
