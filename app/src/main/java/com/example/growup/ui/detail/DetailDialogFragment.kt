package com.example.growup.ui.detail


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.growup.GrowUpApplication
import com.example.growup.R
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.growup.data.user.model.User
import com.example.growup.data.market.model.Products
import com.example.growup.ui.user.UserActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailDialogFragment : DialogFragment() {

    private var detailImage: ImageView? = null
    private var detailProduct: TextView? = null
    private var detailUnitPrice: TextView? = null
    private var detailSize: TextView? = null
    private var detailTotalPrice: TextView? = null
    private var detailUserName: TextView? = null
    private var detailLocation: TextView? = null
    private var detailUserPhone: TextView? = null
    private var detailDate: TextView? = null
    private var detailMessage: TextView? = null
    private var detailGetUser: LinearLayout? = null
    private var detailUserIcon: ImageView? = null
    private var favoriteBtn: CheckBox? = null
    private var whatsappBtn: Button? = null
    private var dialerBtn: Button? = null
    private var soldBtn: Button? = null
    private var favoritesList: ArrayList<String> = ArrayList()
    private lateinit var productKey: String
    private lateinit var from: String
    private lateinit var mData: Products


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_dialog, container, false)
        getData()
        init(view)
        showData()
        return view
    }

    private fun init(view: View) {
        detailImage = view.findViewById(R.id.detail_image)
        detailProduct = view.findViewById(R.id.detail_product)
        detailUnitPrice = view.findViewById(R.id.detail_unit_price)
        detailSize = view.findViewById(R.id.detail_size)
        detailTotalPrice = view.findViewById(R.id.detail_total_price)
        detailUserName = view.findViewById(R.id.detail_user)
        detailLocation = view.findViewById(R.id.detail_location)
        detailUserPhone = view.findViewById(R.id.detail_user_phone)
        detailUserIcon = view.findViewById(R.id.user_icon)
        detailGetUser = view.findViewById(R.id.get_user)
        whatsappBtn = view.findViewById(R.id.whatsapp_btn)
        dialerBtn = view.findViewById(R.id.call_btn)
        detailMessage = view.findViewById(R.id.detail_message)
        soldBtn = view.findViewById(R.id.sold_btn)
        favoriteBtn = view.findViewById(R.id.favorite_btn)
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        if (mData.productImage.isEmpty()) {
            when (mData.category) {
                "Овощи" -> detailImage?.setImageResource(R.drawable.vegetables3)
                "Фрукты" -> detailImage?.setImageResource(R.drawable.fruits2)
                "Животные" -> detailImage?.setImageResource(R.drawable.animals)
                "Другое" -> detailImage?.setImageResource(R.drawable.vegetables2)
            }
        } else {
            Glide.with(activity!!).load(Uri.parse(mData.productImage))
                .placeholder(R.drawable.vegetables3)
                .apply(RequestOptions().override(1500, 2000))
                .into(detailImage!!)
        }

        if (mData.category == "Животные") {
            detailUnitPrice?.text = "Цена за 1 шт: ${mData.unitPrice}"
            detailSize?.text = "Количество: ${mData.size}"
        }else{
            detailUnitPrice?.text = "Цена за 1 кг: ${mData.unitPrice}"
            detailSize?.text = "Объем: ${mData.size}"
        }

        GrowUpApplication.mStorage.child("UsersProfileImages").child(mData.uid).downloadUrl
            .addOnSuccessListener { task ->
                activity?.let { detailUserIcon?.let { it1 -> Glide.with(it).load(task).into(it1) } }
            }.addOnFailureListener {
                detailUserIcon?.setImageResource(R.drawable.user_icon)
            }
        detailProduct?.text = "${mData.name}, ${mData.category}, ${mData.subCategory}"
        detailTotalPrice?.text = "Общая стоимость: ${mData.totalPrice}"
        detailMessage?.text = mData.message

        GrowUpApplication.mUserRef.child(mData.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                detailUserName?.text = "${mData.user}"
                detailUserPhone?.text = "Телефон: ${mData.userPhone}"
                detailLocation?.text = "Местоположение: ${mData.location}"
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(User::class.java)
                detailUserName?.text = "${userData?.name}"
                detailUserPhone?.text = "Телефон: ${userData?.phoneNumber}"
                detailLocation?.text = "Местоположение: ${userData?.region}"
            }
        })

        if (mData.uid != GrowUpApplication.mAuth.currentUser?.uid || from == "SalesFragment") {
            soldBtn?.visibility = View.GONE
        }
        soldBtn?.setOnClickListener {
            activity?.let { it1 -> Utils.confirmationForSold(it1, mData, productKey) }
        }

        whatsappBtn?.setOnClickListener {
            openWhatsApp(mData.userPhone)
        }

        dialerBtn?.setOnClickListener {
            openDialer(mData.userPhone)
        }

        detailGetUser?.setOnClickListener {
            activity?.let { it1 -> UserActivity.start(it1, mData.uid) }
            this.dismiss()
        }

        favoriteBtn?.setOnClickListener {
                if (!favoriteBtn?.isChecked!!) {
                    GrowUpApplication.mUserRef
                        .child(GrowUpApplication.mAuth.currentUser?.uid!!)
                        .child("favorites")
                        .child(productKey)
                        .removeValue()
                    Toast.makeText(activity, "Удалено из Избранного", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(activity, "Добавлено в Избранное", Toast.LENGTH_LONG).show()
                    if (!favoritesList.contains(productKey)) {
                        favoritesList.add(productKey)
                    }
                    favoritesList.forEach {
                        GrowUpApplication.mUserRef
                            .child(GrowUpApplication.mAuth.currentUser?.uid!!)
                            .child("favorites")
                            .child(it)
                            .setValue(true)
                    }
                }
        }
    }

    private fun getData() {
        GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).child("favorites")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(activity, databaseError.message, Toast.LENGTH_LONG).show()
                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        it.key?.let { it1 ->
                            if (!favoritesList.contains(it1)) {
                                favoritesList.add(it1)
                            }
                        }
                        if (favoritesList.contains(productKey)) {
                            favoriteBtn?.isChecked = true
                        }
                    }
                }
            })
    }

    private fun openWhatsApp(number: String) {
        val url = "https://api.whatsapp.com/send?phone=$number"
        try {
            val pm = context?.getPackageManager()
            pm?.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(activity, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun openDialer(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_KEY = "DETAIL_DATA"
        fun newInstance(key: String, from: String, mData: Products): DetailDialogFragment =
            DetailDialogFragment().apply {
                this.productKey = key
                this.from = from
                this.mData = mData
            }
    }

}
