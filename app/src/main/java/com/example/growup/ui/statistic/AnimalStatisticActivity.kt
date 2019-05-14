package com.example.growup.ui.statistic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.statistic.model.ChildList
import com.example.growup.data.statistic.model.ParentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AnimalStatisticActivity : AppCompatActivity() {

    private var statisticRecycler: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_statistic)

        init()

        initData()

    }

    private fun init() {
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        statisticRecycler = findViewById(R.id.statistic_recycler)
        statisticRecycler?.layoutManager = LinearLayoutManager(this)

    }

    private fun initData() {
        GrowUpApplication.mAnimalStatisticRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val parentList = ArrayList<ParentList>()
                dataSnapshot.children.forEach {
                    val parentKey = it.key.toString()
                    GrowUpApplication.mAnimalStatisticRef.child(parentKey).addValueEventListener(object : ValueEventListener,
                        ExpandableRecyclerAdapter.Listener {
                        override fun onCancelled(databaseError1: DatabaseError) {

                        }

                        override fun onDataChange(dataSnapshot1: DataSnapshot) {
                            val childList = ArrayList<ChildList>()
                            dataSnapshot1.children.forEach {
                                childList.add(ChildList(it.key.toString()))
                            }
                            parentList.add(ParentList(parentKey, childList))
                            val adapter = ExpandableRecyclerAdapter(parentList, this@AnimalStatisticActivity, this)
                            statisticRecycler?.adapter = adapter
                        }

                        override fun onItemSelectedAt(key: String, childKey: String?) {
                            LineChartActivity.start(this@AnimalStatisticActivity,key, childKey!!) }
                        }
                    )
                }
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
