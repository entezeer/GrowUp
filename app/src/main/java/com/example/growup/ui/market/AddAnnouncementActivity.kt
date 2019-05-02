package com.example.growup.ui.market

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.*
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.models.ProductsCategories
import com.example.growup.models.Regions
import com.example.growup.ui.main.MainActivity

class AddAnnouncementActivity : AppCompatActivity() {

    private var backButton: FloatingActionButton? = null
    private var name: EditText? = null
    private var unitPrice: EditText? = null
    private var size: EditText? = null
    private var totalPrice: EditText? = null
    private var message: EditText? = null
    private var spinnerCategories: Spinner? = null
    private var spinnerSubCategories: Spinner? = null
    private var spinnerCurrency: Spinner? = null
    private var spinnerSize: Spinner? = null
    private var spinnerCurrencyTotal: Spinner? = null
    private var addBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)

        init()
    }

    private fun init() {
        backButton = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            onBackPressed()
        }

        name = findViewById(R.id.name)
        unitPrice = findViewById(R.id.unit_price)
        size = findViewById(R.id.size)
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

    private fun addAnnouncement() {
        val product = Products(
            name?.text.toString(),
            spinnerCategories?.selectedItem.toString(),
            spinnerSubCategories?.selectedItem.toString(),
            size?.text.toString() + " " + spinnerSize?.selectedItem.toString(),
            unitPrice?.text.toString() + " " + spinnerCurrency?.selectedItem.toString(),
            totalPrice?.text.toString() + " " + spinnerCurrencyTotal?.selectedItem.toString(),
            GrowUpApplication.mUserData.name,
            GrowUpApplication.mUserData.phoneNumber,
            GrowUpApplication.mUserData.region,
            message?.text.toString(),
            GrowUpApplication.mAuth.currentUser?.uid!!
        )
        GrowUpApplication.mMarketRef.child((GrowUpApplication.productsData.size + 1).toString()).setValue(product)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    MainActivity.start(this,"market")
                }
            }
    }
}
