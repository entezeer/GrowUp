package com.growup.growup.ui.user

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.growup.growup.ui.market.sale.OnSaleFragment
import com.growup.growup.ui.market.sale.SalesFragment

class SaleViewPagerAdapter(fm: FragmentManager, private val context: Context, private val counts: Int, private val uid: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when(position){
            0-> OnSaleFragment.newInstance(position, uid)
            1-> SalesFragment.newInstance(position, uid)
            else -> null
        }
    }

    override fun getCount(): Int {
        return counts
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> OnSaleFragment.TITLE
            1-> SalesFragment.TITLE
            else -> null
        }
    }

}