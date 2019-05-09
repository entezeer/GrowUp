package com.example.growup.ui.main

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.bumptech.glide.Glide
import com.example.growup.models.User
import com.example.growup.ui.profile.ProfileActivity
import com.example.growup.ui.SettingsActivity
import com.example.growup.ui.SplashActivity
import com.example.growup.ui.market.MarketFragment
import com.example.growup.ui.search.SearchActivity
import com.example.growup.ui.statistic.StatisticFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var navigationDrawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var userName: TextView? = null
    private var userImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getStringExtra(EXTRA_FRAGMENT) == "market") {
            setFragment(MarketFragment(),"Маркет")
        } else setFragment(StatisticFragment(),"Статистика")

        setUserData()

        init()

    }

    @SuppressLint("NewApi")
    private fun init() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationDrawer = findViewById(R.id.navigation_drawer)

        userName = navigationDrawer?.getHeaderView(0)?.findViewById(R.id.user_name)
        userImage = navigationDrawer?.getHeaderView(0)?.findViewById(R.id.user_icon)
        navigationDrawer?.getHeaderView(0)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }



        userImage = navigationDrawer?.getHeaderView(0)?.findViewById(R.id.user_icon)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu_24_white)
        }

        navigationDrawer?.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_main -> setFragment(StatisticFragment(),"Статистика")
//                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_market -> setFragment(MarketFragment(), "Маркет")
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
        GrowUpApplication.mStorage.child("UsersProfileImages").child(GrowUpApplication.mAuth.currentUser!!.uid)
            .downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@MainActivity).load(task).into(userImage!!)
            }.addOnFailureListener {
                userImage?.setImageResource(R.drawable.user_icon)
            }

        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                GrowUpApplication.mUserData = dataSnapshot.getValue(User::class.java)!!
                val mUserData: Map<*, *> = dataSnapshot.value as Map<*, *>
                if (mUserData["userType"]=="Оптовик"){
                    setFragment(MarketFragment(), "Маркет")
                }
                userName?.text = "${mUserData["name"]} ${mUserData["lastName"]}"

            }
        })
    }

    private fun setFragment(fragment: Fragment, title: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
        supportActionBar?.title = title
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

    companion object {
        private const val EXTRA_FRAGMENT = "fragment"
        fun start(context: Context, fragment: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT, fragment)
            context.startActivity(intent)
        }
    }
}
