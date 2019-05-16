package com.example.growup.ui.start

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.Button
import com.example.growup.R
import com.example.growup.ui.auth.login.LoginActivity
import com.example.growup.ui.auth.register.RegisterActivity
import java.util.*

class StartActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    private var indicator: TabLayout? = null
    private var login: Button? = null
    private var register: Button? = null

    private var listImages: MutableList<Drawable> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        init()
//        setImages()
//
//        viewPager?.adapter = ViewPagerAdapter(this, listImages)
//        indicator?.setupWithViewPager(viewPager,true)
//
//        val timer = Timer()
//        timer.scheduleAtFixedRate(SliderTimer(),2000,4000)
    }

    private fun init() {
        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.indicator)
        login = findViewById(R.id.log_in)
        login?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        register = findViewById(R.id.register)
        register?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

//    private fun setImages() {
//        listImages.add(resources.getDrawable(R.drawable.grow1))
//        listImages.add(resources.getDrawable(R.drawable.grow2))
//        listImages.add(resources.getDrawable(R.drawable.grow3))
//    }

    inner class SliderTimer: TimerTask(){
        override fun run() {
            this@StartActivity.runOnUiThread {
                if(viewPager?.currentItem!! < listImages.size-1){
                    viewPager?.currentItem = viewPager?.currentItem!! +1
                }
                else viewPager?.currentItem = 0
            }
        }
    }
}
