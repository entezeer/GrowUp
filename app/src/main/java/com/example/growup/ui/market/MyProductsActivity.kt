package com.example.growup.ui.market

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.ui.user.SaleViewPagerAdapter

class MyProductsActivity : AppCompatActivity(){

    private var PAGE_COUNTS = 2
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        init()
    }

        private fun init(){
            supportActionBar?.title = "Мои объявления"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

            viewPager = findViewById(R.id.view_pager)
            tabLayout = findViewById(R.id.tabs)

            viewPager?.adapter = GrowUpApplication.mAuth.currentUser?.uid?.let {
                SaleViewPagerAdapter(supportFragmentManager, this, PAGE_COUNTS,
                    it
                )
            }
            tabLayout?.setupWithViewPager(viewPager)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
