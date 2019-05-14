package com.example.growup.data.statistic.remote

import com.example.core.firebase.FirebaseClient
import com.example.growup.data.statistic.StatisticDataSource
import com.example.growup.data.statistic.model.ChildList
import com.example.growup.data.statistic.model.ParentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class StatisticRemote : FirebaseClient(), StatisticDataSource {

    companion object {
        private var INSTANCE: StatisticRemote? = null
        fun getInstance(): StatisticRemote {
            if (INSTANCE == null) {
                INSTANCE = StatisticRemote()
            }
            return INSTANCE!!
        }
    }

    private var statisticRef = getRef(StatisticRemoteConstants.REF_KEY)

    override fun getStatistic(callback: StatisticDataSource.RequestCallback) {
        val parentList = ArrayList<ParentList>()
        statisticRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                callback.onFailure(databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    statisticRef.child(it.key.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(databaseError1: DatabaseError) {
                            callback.onFailure(databaseError1.message)
                        }

                        override fun onDataChange(dataSnapshot1: DataSnapshot) {
                            val childList = ArrayList<ChildList>()
                            dataSnapshot1.children.forEach { it1 ->
                                childList.add(ChildList(it1.key.toString()))
                            }
                            parentList.add(
                                ParentList(
                                    it.key.toString(),
                                    childList
                                )
                            )
                            callback.onSuccess(parentList)
                        }

                    })
                }
            }

        })
    }

    override fun setStatistic() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}