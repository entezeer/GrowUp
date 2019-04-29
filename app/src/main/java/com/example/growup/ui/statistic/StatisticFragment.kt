package com.example.growup.ui.statistic


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.growup.R
import com.example.growup.models.Regions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StatisticFragment : Fragment() {

    private var statisticRecycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_statistic, container, false)

        init(rootView)

        return rootView
    }

    fun init(view: View){
        statisticRecycler = view.findViewById(R.id.statistic_recycler)
        statisticRecycler?.layoutManager = LinearLayoutManager(activity)
        statisticRecycler?.adapter = activity?.let { CategoryAdapter(Regions.regions, it) }
    }

}
