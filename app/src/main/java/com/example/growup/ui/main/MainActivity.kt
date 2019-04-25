package com.example.growup.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.growup.*
import com.example.growup.models.User
import com.example.growup.ui.ProfileActivity
import com.example.growup.ui.SearchActivity
import com.example.growup.ui.SettingsActivity
import com.example.growup.ui.SplashActivity
import com.example.growup.ui.fragments.MarketFragment
import com.example.growup.ui.fragments.StatisticFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private var navigationDrawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var userName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        setragment(StatisticFragment())
    }

    @SuppressLint("NewApi")
    private fun init() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationDrawer = findViewById(R.id.navigation_drawer)

        userName = navigationDrawer?.getHeaderView(0)?.findViewById(R.id.user_name)

        setUserData()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu_24_white)
        }



        navigationDrawer?.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.user_icon->startActivity(Intent(this, ProfileActivity::class.java))
                R.id.user_name->startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_main -> setragment(StatisticFragment())
                R.id.nav_search ->startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_market -> setragment(MarketFragment())
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_log_out -> {
                    GrowUpApplication.mAuth.signOut()
                    startActivity(Intent(this, SplashActivity::class.java))
                    finishAffinity()
                }
            }
            drawerLayout?.closeDrawers()
            true
        }
    }

    private fun setUserData() {
        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mUserData = Gson().fromJson(dataSnapshot.value.toString(), User::class.java)
                userName?.text = mUserData.name
            }
        })
    }

    private fun setragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
