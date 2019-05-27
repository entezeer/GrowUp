package com.growup.growup.ui.market


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growup.growup.GrowUpApplication
import com.growup.growup.R
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.growup.growup.data.user.model.User
import com.growup.growup.data.market.model.Products
import com.growup.growup.ui.user.UserActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MarketSortFragment : DialogFragment() {
    private var mData: ArrayList<Products> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sort_products, container, false)
        this.dialog.setCanceledOnTouchOutside(true)
        init(view)

        return view
    }

    private fun init(view: View) {

    }


    companion object {

        fun newInstance(mData: ArrayList<Products>): MarketSortFragment =
           MarketSortFragment().apply {
                this.mData = mData
            }
    }

}
