package com.growup.growup.ui.market

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MarketViewPagerAdapter(fm: FragmentManager, private val counts: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when(position){
            0-> ProductsFragment.newInstance(position)
            1-> OrderFragment.newInstance(position)
            else -> null
        }
    }

    override fun getCount(): Int {
        return counts
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> ProductsFragment.TITLE
            1-> OrderFragment.TITLE
            else -> null
        }
    }

}