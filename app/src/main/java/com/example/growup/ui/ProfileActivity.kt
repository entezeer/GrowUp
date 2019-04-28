package com.example.growup.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
=======
import android.support.design.widget.FloatingActionButton
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.growup.GrowUpApplication
>>>>>>> 50484cae6a2af01d50d4bf0e319e7157ae4af90e
import com.example.growup.R
import com.example.growup.models.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.w3c.dom.Text
import java.io.File
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {
<<<<<<< HEAD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
=======
    private var userImage: Uri? = null
    private var editProfileButton: Button? = null
    private var backButton: FloatingActionButton? = null
    private var profileImage: ImageView? = null
    private var profileName: TextView? = null
    private var profileUserType: TextView? = null
    private var profileSurname: TextView? = null
    private var profileEmail: TextView? = null
    private var profilePhoneNumber: TextView? = null
    private var profileRegion: TextView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        getUserData()
    }


    override fun onResume() {
        super.onResume()
        getUserData()
    }

    private fun getUserData() {
        GrowUpApplication.mStorage.child("UsersProfileImages").child(GrowUpApplication.mAuth.currentUser!!.uid).downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@ProfileActivity).load(task).into(profileImage!!)
            }.addOnFailureListener {
                profileImage?.setImageResource(R.drawable.user_icon)
            }

        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userData = Gson().fromJson(dataSnapshot.value.toString(),User::class.java)
                    profileName?.text = userData.name
                    profileSurname?.text = userData.lastName
                    profileUserType?.text = userData.userType
                    profileEmail?.text = userData.email
                    profilePhoneNumber?.text = userData.phoneNumber
                    profileRegion?.text = userData.region
                }
            })
    }

    private fun init(){
        backButton = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            onBackPressed()
        }

        editProfileButton = findViewById(R.id.edit_profile_btn)
        editProfileButton?.setOnClickListener{
            startActivity(Intent(this,EditProfileActivity::class.java))
        }

        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.profile_user_name)
        profileSurname = findViewById(R.id.profile_user_surname)
        profilePhoneNumber = findViewById(R.id.profile_phone_number)
        profileRegion = findViewById(R.id.profile_user_region)
        profileEmail = findViewById(R.id.profile_email)
        profileUserType = findViewById(R.id.profile_user_type)
>>>>>>> 50484cae6a2af01d50d4bf0e319e7157ae4af90e
    }
}
