package com.example.growup.ui.user

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat.startActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.user.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    private var PAGE_COUNTS = 2
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var uid: String? = null
    private var backButton: FloatingActionButton? = null
    private var profileImage: ImageView? = null
    private var profileName: TextView? = null
    private var profilePhoneNumber: TextView? = null
    private var profileRegion: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        uid = intent.getStringExtra(EXTRA_UID)

        init()

        showData()
    }

    private fun init(){
        supportActionBar?.title = "Профиль"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabs)

        viewPager?.adapter = SaleViewPagerAdapter(supportFragmentManager, this, PAGE_COUNTS, uid!!)
        tabLayout?.setupWithViewPager(viewPager)

        backButton = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            onBackPressed()
        }

        profileImage = findViewById(R.id.profile_image)
        profileName = findViewById(R.id.profile_user_name)
        profilePhoneNumber = findViewById(R.id.profile_phone_number)
        profileRegion = findViewById(R.id.profile_user_region)
    }
    private fun showData(){
        GrowUpApplication.mStorage.child("UsersProfileImages").child(uid!!).downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@UserActivity).load(task).into(profileImage!!)
            }.addOnFailureListener {
                profileImage?.setImageResource(R.drawable.user_icon)
            }

        GrowUpApplication.mUserRef.child(uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@UserActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userData = dataSnapshot.getValue(User::class.java)
                    profileName?.text = userData?.name
                    profilePhoneNumber?.text = userData?.phoneNumber
                    profileRegion?.text = userData?.region
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object{
        private val EXTRA_UID = "uid"
        fun start(context: Context,uid: String){
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(EXTRA_UID, uid)
            startActivity(context, intent, Bundle())
        }
    }
}
