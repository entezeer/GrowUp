package com.example.growup.ui.profile

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.user.model.User
import com.example.growup.ui.market.FavoritesActivity
import com.example.growup.ui.market.MyProductsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {

    private var userImage: Uri? = null
    private var editProfileButton: Button? = null
    private var getMyProducts: Button? = null
    private var getMyFavorite: Button? = null
    private var backButton: FloatingActionButton? = null
    private var profileImage: ImageView? = null
    private var profileName: TextView? = null
    private var profileUserType: TextView? = null
    private var profileSurname: TextView? = null
    private var profileEmail: TextView? = null
    private var profilePhoneNumber: TextView? = null
    private var profileRegion: TextView? = null
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
        GrowUpApplication.mStorage.child("UsersProfileImages").child(GrowUpApplication.mAuth.currentUser!!.uid)
            .downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@ProfileActivity).load(task).into(profileImage!!)
            }.addOnFailureListener {
                profileImage?.setImageResource(R.drawable.user_icon)
            }

        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userData = dataSnapshot.getValue(User::class.java)
                    profileName?.text = userData?.name
                    profileSurname?.text = userData?.lastName
                    profileUserType?.text = userData?.userType
                    profilePhoneNumber?.text = userData?.phoneNumber
                    profileRegion?.text = userData?.region
                    if(userData?.email!!.isEmpty() || userData.email.length <= 5){
                        profileEmail?.visibility = View.GONE
                    }
                    profileEmail?.text = userData?.email
                }
            })
    }

    private fun init() {
        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }

        editProfileButton = findViewById(R.id.edit_profile_btn)
        editProfileButton?.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        getMyProducts = findViewById(R.id.my_products)
//        if (GrowUpApplication.mUserData.userType == "Оптовик") {
//            getMyProducts?.visibility = View.GONE
//        }

        getMyFavorite = findViewById(R.id.favorite)
        getMyFavorite?.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        getMyProducts?.setOnClickListener {
            startActivity(Intent(this, MyProductsActivity::class.java))
        }

        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.profile_user_name)
        profileSurname = findViewById(R.id.profile_user_surname)
        profilePhoneNumber = findViewById(R.id.profile_phone_number)
        profileRegion = findViewById(R.id.profile_user_region)
        profileEmail = findViewById(R.id.profile_email)
        profileUserType = findViewById(R.id.profile_user_type)

    }
}
