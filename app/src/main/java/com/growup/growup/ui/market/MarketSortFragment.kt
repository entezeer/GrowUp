package com.growup.growup.ui.market


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.growup.growup.R
import com.growup.growup.data.market.model.Products
import com.growup.growup.models.ProductsCategories
import com.growup.growup.models.Regions
import kotlinx.android.synthetic.main.fragment_sort_products.*

class MarketSortFragment : DialogFragment(){
    private var mData: ArrayList<Products> = ArrayList()
    private var spinnerRegions: Spinner? = null
    private var spinnerCategory: Spinner? = null
    private var spinnerSubcategory: Spinner? = null
    private var buttonCancel: Button? = null
    private var buttonOk: Button? = null
    private var getData: GetSortData? = null
    var sortTitle: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sort_products, container, false)
        this.dialog.setCanceledOnTouchOutside(true)
        init(view)

        return view
    }


    private fun init(view: View) {
        sortTitle.add("")
        spinnerRegions = view.findViewById(R.id.sort_spinner_regions)
        spinnerRegions?.adapter =
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, sortTitle+Regions.regions)


        spinnerSubcategory = view.findViewById(R.id.sort_spinner_subcategories)
        spinnerCategory = view.findViewById(R.id.sort_spinner_categories)
        spinnerCategory?.adapter =
            ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                sortTitle+ProductsCategories.productCategory
            )
        spinnerCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinnerCategory?.selectedItemPosition!! == 0){
                    spinnerSubcategory?.adapter =
                        ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            sortTitle
                        )
                }else{
                    spinnerSubcategory?.adapter =
                        ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            sortTitle+ProductsCategories.categoryList[spinnerCategory?.selectedItemPosition!!-1]
                        )
                }
            }
        }


        buttonCancel = view.findViewById(R.id.sort_cancel)
        buttonCancel?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
            this.dismiss()
        }

        buttonOk = view.findViewById(R.id.sort_ok)
        buttonOk?.setOnClickListener {
            sortProducts()
        }
    }

    private fun sortProducts() {
        val productLocation: String = sort_spinner_regions?.selectedItem.toString().trim()
        val productCategory: String = sort_spinner_categories?.selectedItem.toString().trim()
        val productSubCategory: String = sort_spinner_subcategories?.selectedItem.toString().trim()

        getData?.getOrderData(productCategory,productSubCategory,productLocation)
        activity?.supportFragmentManager?.popBackStack()
        this.dismiss()

    }


    companion object {

        fun newInstance(mData: ArrayList<Products>, getData: GetSortData): MarketSortFragment =
           MarketSortFragment().apply {
                this.mData = mData
                this.getData = getData
            }
    }

    interface GetSortData{
        fun getOrderData(category:String ,subCategory:String ,region:String)
    }
}

