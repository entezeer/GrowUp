package com.growup.growup.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.growup.core.firebase.FirebaseClient
import com.growup.growup.R
import com.growup.growup.data.RepositoryProvider
import com.growup.growup.data.user.UserDataSource
import com.growup.growup.data.user.model.User
import com.growup.growup.ui.market.FavoritesActivity
import com.growup.growup.ui.market.MyProductsActivity


class ProfileActivity : AppCompatActivity() {

    private var userImage: Uri? = null
    private var editProfileButton: Button? = null
    private var getMyProducts: Button? = null
    private var getMyFavorite: Button? = null
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

        RepositoryProvider.getUserDataSource()
            .getUser(FirebaseClient().getAuth().currentUser?.uid!!, object : UserDataSource.UserCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(result: User) {
                    if (result.profileImage.isNotEmpty()) {
                        if (!isFinishing) Glide.with(this@ProfileActivity)
                            .load(result.profileImage)
                            .placeholder(R.drawable.user_icon)
                            .into(profileImage!!)
                    } else profileImage?.setImageResource(R.drawable.user_icon)

                    profileName?.text = result.name
                    profileSurname?.text = result.lastName
                    profileUserType?.text = result.userType
                    profilePhoneNumber?.text = result.phoneNumber
                    profileRegion?.text = result.region
                    if (result.email.isEmpty() || result.email.length <= 5) {
                        profileEmail?.visibility = View.GONE
                    }
                    profileEmail?.text = result.email
                }

                override fun onFailure(message: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }

    private fun init() {
        supportActionBar?.title = "Профиль"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
