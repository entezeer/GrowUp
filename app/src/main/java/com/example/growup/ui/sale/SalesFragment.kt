package com.example.growup.ui.sale


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.growup.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SalesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sales, container, false)


        return view
    }


    companion object {
        private const val ARG_PAGE = "ARG_FREE"
        private const val ARG_STATUS_CODE = 0
        private const val ARG_POSITION = "position"
        private const val ARG_UID = "uid"
        const val TITLE = "Продано"
        var INSTANCE: Fragment? = null
        fun newInstance(position: Int, uid: String): Fragment {
            val fragment = SalesFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            bundle.putString(ARG_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }
}
