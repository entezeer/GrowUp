package com.example.growup.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.models.User
import com.example.growup.ui.main.MainActivity
import com.example.growup.ui.start.StartActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
                checkUser()
            }, SPLASH_TIME_OUT
        )

    }

    private fun checkUser() {
        if (GrowUpApplication.mAuth.currentUser != null) {
            GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SplashActivity, databaseError.message, Toast.LENGTH_LONG).show()
                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val mUserData: Map<String, String> = dataSnapshot.value as Map<String, String>
                    if (mUserData["userType"] == "Оптовик") {
                        MainActivity.start(this@SplashActivity, "market")
                    } else MainActivity.start(this@SplashActivity, "statistic")

                }
            })
        } else startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}
