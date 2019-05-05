package com.example.growup.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.*
import com.bumptech.glide.Glide
import com.entezeer.tracking.utils.ValidUtils
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.SyncTree
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson

class EditProfileActivity : AppCompatActivity() {
    private val GET_IMAGE_GALLERY: Int = 1
    private var imageUri: Uri? = null
    private var buttonSaveChanges: Button? = null
    private var buttonBack: FloatingActionButton? = null
    private var editProfileImage: ImageView? = null
    private var editProfileName: EditText? = null
    private var editProfileSurname: EditText? = null
    private var editProfileEmail: EditText? = null
    private var editProfileRegion: EditText?= null

//    private var editProfilePhoneNumber: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        init()
        getUserData()
    }

    private fun getUserData() {
        GrowUpApplication.mStorage.child("UsersProfileImages").child(GrowUpApplication.mAuth.currentUser!!.uid).downloadUrl
            .addOnSuccessListener { task ->
                Glide.with(this@EditProfileActivity).load(task).into(editProfileImage!!)
            }.addOnFailureListener {
                editProfileImage?.setImageResource(R.drawable.user_icon)
            }


        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@EditProfileActivity, p0.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data =dataSnapshot.getValue(User::class.java)
                    editProfileName?.setText(data?.name)
                    editProfileSurname?.setText(data?.lastName)
                    editProfileEmail?.setText(data?.email)
                    editProfileRegion?.setText(data?.region)

                }
            })
    }

    private fun init(){
        buttonBack = findViewById(R.id.back_button)
        buttonBack?.setOnClickListener {
            onBackPressed()
        }
        buttonSaveChanges = findViewById(R.id.save_user_data_btn)
        buttonSaveChanges?.setOnClickListener {
            if (ValidUtils.checkEditProfileChanges(editProfileName!! , editProfileSurname!!, editProfileRegion!!, editProfileEmail!!)){
                saveChanges()
            }
        }
        editProfileImage = findViewById(R.id.edit_profile_image)
        editProfileImage?.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Выбирите изображение"),GET_IMAGE_GALLERY)
        }
        editProfileName = findViewById(R.id.edit_profile_name)
        editProfileSurname = findViewById(R.id.edit_profile_surname)
        editProfileEmail = findViewById(R.id.edit_profile_email)
        editProfileRegion = findViewById(R.id.edit_profile_region)
    }
    private fun saveChanges(){
            if (imageUri != null){
                val dialog: ProgressDialog = ProgressDialog(this)
                dialog.setCancelable(false)
                dialog.setTitle("Изображение загружается")
                dialog.show()
                GrowUpApplication.mStorage.child("UsersProfileImages/"+GrowUpApplication.mAuth.currentUser!!.uid)
                    .putFile(imageUri!!).addOnSuccessListener{ task ->
                        dialog.dismiss()
                        onBackPressed()
                    }.addOnFailureListener{task ->
                        dialog.dismiss()
                        Toast.makeText(this@EditProfileActivity, task.message.toString() ,Toast.LENGTH_SHORT).show()
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred/it.totalByteCount
                        dialog.setMessage("Загружено " + progress.toInt() + "%")
                    }
            }
                GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid).child("lastName").setValue(editProfileSurname?.text.toString())
                GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid).child("name").setValue(editProfileName?.text.toString())
                GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid).child("email").setValue(editProfileEmail?.text.toString())
                GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser!!.uid).child("region").setValue(editProfileRegion?.text.toString())
                onBackPressed()
            }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GET_IMAGE_GALLERY){
               imageUri = data?.data
               editProfileImage?.setImageURI(imageUri)
        }
    }
}
