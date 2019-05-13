package com.example.growup.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.*
import com.bumptech.glide.Glide
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.User
import com.example.growup.ui.profile.ProfileActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SettingsActivity : AppCompatActivity() {
    private var userInfo: RelativeLayout? = null
    private var backButton: FloatingActionButton? = null
    private var buttonRate: Button? = null
    private var buttonShare: Button? = null
    private var buttonReportBug: Button? = null
    private var spinner: Spinner? = null
    private var userImage: ImageView? = null
    private var userName: TextView? = null
    private var userType: TextView? = null
    private var buttonAboutUs: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        init()
        initUserInfo()
    }

    private fun initUserInfo() {

        GrowUpApplication.mStorage.child("UsersProfileImages").child(GrowUpApplication.mAuth.currentUser!!.uid).downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@SettingsActivity).load(task).into(userImage!!)
            }.addOnFailureListener {
                userImage?.setImageResource(R.drawable.user_icon)
            }


        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(User::class.java)
                userName?.text = data?.name + " " + data?.lastName
                userType?.text = data?.userType
            }

        })
    }


    private fun init(){
        val country = arrayOf("Русский","Кыргызский")
        spinner = findViewById(R.id.settings_lang_spinner)
        spinner?.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,country)
        buttonAboutUs = findViewById(R.id.button_about_us)
        buttonAboutUs?.setOnClickListener {
            startActivity(Intent(this@SettingsActivity,AboutUsActivity::class.java))
        }

        userImage = findViewById(R.id.settings_user_image)
        userName = findViewById(R.id.settings_user_name)
        userType = findViewById(R.id.settings_user_type)


        backButton = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            onBackPressed()
        }

        userInfo = findViewById(R.id.settigs_user_info)
        userInfo?.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, ProfileActivity::class.java))
        }
        buttonRate = findViewById(R.id.button_rate_app)
        buttonRate?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"))
            startActivity(intent)
        }

        buttonReportBug = findViewById(R.id.button_report_bug)
        buttonReportBug?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:growupapp2019@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT,"ERROR_REPORT")
            startActivity(Intent.createChooser(intent,"Send Report"))
        }

        buttonShare = findViewById(R.id.button_share_app)
        buttonShare?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "GrowUp APP")
            intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
            startActivity(Intent.createChooser(intent,"Share to "))
        }
    }
}
