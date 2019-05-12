package com.example.growup.ui.statistic


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyCharacterMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Regions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StatisticFragment : Fragment()     {

    private var animalStatistic: CardView? = null
    private var statisticRecycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_statistic, container, false)

        init(rootView)
        showData()

        return rootView
    }

    fun init(view: View){
        statisticRecycler = view.findViewById(R.id.statistic_recycler)
        statisticRecycler?.layoutManager = LinearLayoutManager(activity)
//        statisticRecycler?.adapter = activity?.let { CategoryAdapter(Regions.regions, it) }

        animalStatistic = view.findViewById(R.id.animal_statistic)
        animalStatistic?.setOnClickListener {
            startActivity(Intent(activity,AnimalStatisticActivity::class.java))
        }
    }

    private fun showData(){
        GrowUpApplication.mStatisticRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val parentList = ArrayList<ParentList>()
                dataSnapshot.children.forEach {
                    val parentKey = it.key.toString()
                    GrowUpApplication.mStatisticRef.child(parentKey).addValueEventListener(object : ValueEventListener,
                        ExpandableRecyclerAdapter.Listener {
                        override fun onItemSelectedAt(key: String, childKey: String?) {
                            activity?.let { it1 -> DetailStatisticActivity.start(it1,key, childKey!!) }
                        }


                        override fun onCancelled(databaseError: DatabaseError) {

                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val childList = ArrayList<ChildList>()
                                dataSnapshot.children.forEach {
                                    childList.add(ChildList(it.key.toString()))
                                }
                            parentList.add(ParentList(parentKey,childList))
                            val adapter = activity?.let { it1 -> ExpandableRecyclerAdapter(parentList, it1, this) }
                            statisticRecycler?.adapter = adapter
                        }

                    })
                }
            }

        })
    }
}
