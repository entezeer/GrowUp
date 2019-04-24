package com.example.growup

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.support.v4.view.ViewPager

class ViewPagerAdapter(private val context: Context, var listImages: MutableList<Drawable>) : PagerAdapter() {

    private var layoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int {
        return listImages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = layoutInflater.inflate(R.layout.viewpager_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.img_pager_item)
        imageView.setImageDrawable(listImages[position])

        container.addView(view, 0)

        return view
    }

}