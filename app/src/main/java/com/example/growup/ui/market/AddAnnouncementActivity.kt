package com.example.growup.ui.market

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.annotation.SuppressLint
import android.app.ProgressDialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.entezeer.tracking.utils.ValidUtils
import com.example.core.firebase.FirebaseClient
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.market.model.Products
import com.example.growup.data.user.UserDataSource
import com.example.growup.data.user.model.User
import com.example.growup.models.ProductsCategories
import com.example.growup.ui.main.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddAnnouncementActivity : AppCompatActivity() {
    private val GET_IMAGE_GALLERY = 1
    private val GET_IMAGE_CAMERA = 2
    private val REQUEST_PERMISSION = 3
    private var photoPath: String ? = null
    private var imageUri: Uri? = null
    private var backButton: FloatingActionButton? = null
    private var name: EditText? = null
    private var unitPrice: EditText? = null
    private var productImage:ImageView? = null
    private var size: EditText? = null
    private var totalPrice: EditText? = null
    private var message: EditText? = null
    private var spinnerCategories: Spinner? = null
    private var spinnerSubCategories: Spinner? = null
    private var spinnerCurrency: Spinner? = null
    private var spinnerSize: Spinner? = null
    private var spinnerCurrencyTotal: Spinner? = null
    private var addBtn: Button? = null
    private var animalCountView: TextView? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)

        RepositoryProvider.getUserDataSource()
            .getUser(FirebaseClient().getAuth().currentUser?.uid!!, object : UserDataSource.UserCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(result: User) {
                    GrowUpApplication.mUserData = result
                }
                override fun onFailure(message: String) {
                }
            })

        init()
    }

    private fun init() {
        progressDialog = ProgressDialog(this)
        productImage = findViewById(R.id.add_product_image)
        productImage?.setOnClickListener {
            getImageFrom()
        }
        backButton = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            onBackPressed()
        }
        animalCountView = findViewById(R.id.animalCount)
        name = findViewById(R.id.name)
        unitPrice = findViewById(R.id.unit_price)

        size = findViewById(R.id.size)
        size?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculator()
            }

        })

        unitPrice?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculator()
            }

        })


        totalPrice = findViewById(R.id.total_price)
        message = findViewById(R.id.message)

        spinnerCategories = findViewById(R.id.spinner_categories)
        spinnerSubCategories = findViewById(R.id.spinner_subcategories)
        spinnerCurrency = findViewById(R.id.spinner_currency)
        spinnerSize = findViewById(R.id.spinner_size)
        spinnerCurrencyTotal = findViewById(R.id.spinner_currency_total)

        addBtn = findViewById(R.id.add_btn)
        addBtn?.setOnClickListener {
            addAnnouncement()
        }

        initSpinners()
    }
    private fun getImageFrom() {
//        val items = arrayOf("Камера","Галерея")
//        val builder = android.app.AlertDialog.Builder(this)
//        builder.setTitle("Выбор")
//        builder.setCancelable(true)
//        builder.setItems(items, DialogInterface.OnClickListener { dialog, which ->
//            when (which) {
//                0 -> getImageFromCamera()
//                1 -> getImageFromGallery()
//            }
//        })
//        val dialog: android.app.AlertDialog = builder.create()
//        dialog.show()
        getImageFromGallery()
    }
    private fun getImageFromCamera(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_PERMISSION)
        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager )!= null){
                val photoFile = createPhotoFile()
                photoPath = photoFile.absolutePath
                imageUri = FileProvider.getUriForFile(this@AddAnnouncementActivity,
                    "com.example.growup.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, GET_IMAGE_CAMERA)
            }

        }

    }

    private fun createPhotoFile(): File{
        val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        val storageDirectory: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        var image: File? = null
        try {
            image = File.createTempFile(name, ".jpeg", storageDirectory);
        } catch (e: IOException) {
            Toast.makeText(this@AddAnnouncementActivity,e.toString(),Toast.LENGTH_SHORT).show()
        }
        return image!!
    }

    private fun getImageFromGallery(){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Выберите изображение"),GET_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GET_IMAGE_GALLERY){
            imageUri = data?.data
            productImage?.background = null
            productImage?.setImageURI(imageUri)
        }
//        if (resultCode == Activity.RESULT_OK && requestCode == GET_IMAGE_CAMERA){
//            productImage?.setImageURI(imageUri)
//        }
    }



    private fun initSpinners() {
        spinnerCategories?.adapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ProductsCategories.productCategory
            )

        spinnerCategories?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerSubCategories?.adapter =
                    ArrayAdapter<String>(
                        this@AddAnnouncementActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        ProductsCategories.categoryList[spinnerCategories?.selectedItemPosition!!]
                    )
                if (spinnerCategories?.selectedItemPosition == 2) {
                    size?.hint = getString(R.string.count_animal)
                    unitPrice?.hint = getString(R.string.unit_price_animal)
                    spinnerSize?.visibility = View.GONE
                    animalCountView?.visibility = View.VISIBLE
                } else {
                    size?.hint = getString(R.string.size)
                    unitPrice?.hint = getString(R.string.unit_price)
                    spinnerSize?.visibility = View.VISIBLE
                    animalCountView?.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerCurrency?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ProductsCategories.currency)

        spinnerCurrencyTotal?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ProductsCategories.currency)

        spinnerSize?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ProductsCategories.sizeType)
    }

    private fun calculator() {
        if (spinnerSize?.selectedItem.toString() == ProductsCategories.sizeType[0]) {
            if (unitPrice?.text.toString().trim().isNotEmpty() && size?.text.toString().trim().isNotEmpty()) {
                totalPrice?.setText((unitPrice?.text?.toString()?.toInt()!! * size?.text?.toString()?.toInt()!!).toString())
            }
        }
        if (spinnerSize?.selectedItem.toString() == ProductsCategories.sizeType[1]) {
            if (unitPrice?.text.toString().trim().isNotEmpty() && size?.text.toString().trim().isNotEmpty()) {
                totalPrice?.setText((unitPrice?.text?.toString()?.toInt()!! * (size?.text?.toString()?.toInt()!! * 1000)).toString())
            }
        }
    }

    private fun addAnnouncement() {
        val fileName = UUID.randomUUID().toString()
        if (ValidUtils.checkAddProductData(name!!, unitPrice!!, size!!, totalPrice!!, message!!,productImage!!,this)) {
            Utils.progressShow(progressDialog)
            progressDialog?.setMessage("Данные загружаются, это займет не больше минуты")
            GrowUpApplication.mStorage.child("ProductsImages/$fileName")
                .putFile(imageUri!!).addOnSuccessListener{ task ->
                    GrowUpApplication.mStorage.child("ProductsImages")
                        .child(fileName).downloadUrl
                        .addOnSuccessListener { task ->
                            val product = Products(
                                name?.text.toString(),
                                spinnerCategories?.selectedItem.toString(),
                                spinnerSubCategories?.selectedItem.toString(),
                                size?.text.toString() + " " + if (spinnerCategories?.selectedItemPosition == 2) {
                                    "Шт."
                                } else {
                                    spinnerSize?.selectedItem.toString()
                                },
                                unitPrice?.text.toString() + " " + spinnerCurrency?.selectedItem.toString(),
                                totalPrice?.text.toString() + " " + spinnerCurrencyTotal?.selectedItem.toString(),
                                GrowUpApplication.mUserData.name,
                                GrowUpApplication.mUserData.phoneNumber,
                                GrowUpApplication.mUserData.region,
                                message?.text.toString(),
                                GrowUpApplication.mAuth.currentUser!!.uid,
                                task.toString()
                            )
                            GrowUpApplication.mMarketRef.child("onSale").push().setValue(product)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        progressDialog?.dismiss()
                                        MainActivity.start(this@AddAnnouncementActivity, "Маркет")
                                    }
                                }
                        }

                }.addOnFailureListener{task ->
                    Toast.makeText(this@AddAnnouncementActivity, task.message.toString() ,Toast.LENGTH_SHORT).show()
                }

        }

    }
}
