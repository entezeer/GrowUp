package com.growup.growup.ui.market

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import com.growup.growup.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MarketFragment : Fragment(){

    private var PAGE_COUNTS = 2
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    companion object {
        fun newInstance(): MarketFragment = MarketFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_market, container, false)
        init(view)

        return view
    }

    @SuppressLint("NewApi")
    private fun init(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabs)

        viewPager?.adapter = activity?.supportFragmentManager?.let { MarketViewPagerAdapter(it, PAGE_COUNTS) }
        tabLayout?.setupWithViewPager(viewPager)
    }

}
