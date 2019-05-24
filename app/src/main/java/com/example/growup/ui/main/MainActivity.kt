package com.example.growup.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.bumptech.glide.Glide
import com.example.core.extensions.setFragment
import com.example.core.firebase.FirebaseClient
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.user.UserDataSource
import com.example.growup.data.user.model.User
import com.example.growup.ui.profile.ProfileActivity
import com.example.growup.ui.SettingsActivity
import com.example.growup.ui.SplashActivity
import com.example.growup.ui.market.MarketContract
import com.example.growup.ui.market.MarketFragment
import com.example.growup.ui.statistic.StatisticContract
import com.example.growup.ui.statistic.StatisticFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var mPresenter: StatisticContract.Presenter? = null
    private var mMarketPresenter: MarketContract.Presenter? = null
    private var navigationDrawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var userName: TextView? = null
    private var userImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statisticFragment = StatisticFragment.newInstance()
        val marketFragment = MarketFragment.newInstance()
        setFragment(StatisticFragment.newInstance(), R.id.frame_container, "Статистика")

        if (intent.getStringExtra(EXTRA_FRAGMENT) == "Маркет") {
            setFragment(MarketFragment.newInstance(), R.id.frame_container, "Маркет")
        } else setFragment(StatisticFragment.newInstance(), R.id.frame_container, "Статистика")

        init()
        setUserData()
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
                R.id.nav_statistic -> setFragment(StatisticFragment(), R.id.frame_container, "Статистика")
//                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_market -> setFragment(MarketFragment(), R.id.frame_container, "Маркет")
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

        RepositoryProvider.getUserDataSource()
            .getUser(FirebaseClient().getAuth().currentUser?.uid!!, object : UserDataSource.UserCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(result: User) {
                    if (result.profileImage.isNotEmpty()) {
                        Glide.with(this@MainActivity).load(result.profileImage).into(userImage!!)
                    } else userImage?.setImageResource(R.drawable.user_icon)

                    userName?.text = "${result.name} ${result.lastName}"
                }

                override fun onFailure(message: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Вы уже уходите ?")
        builder.setMessage("Вы действительно хотите выйти ?")
        builder.setCancelable(true)
        builder.setPositiveButton("Да") { dialog, which ->
            finishAffinity()
        }
        builder.setNegativeButton("Нет") { dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
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
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
