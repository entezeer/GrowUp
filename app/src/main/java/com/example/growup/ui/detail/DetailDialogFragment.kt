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
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.example.growup.models.User
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

    private var whatsappBtn: Button? = null
    private var dialerBtn: Button? = null
    private var soldBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_dialog, container, false)
        if (dialog!=null && dialog.window!=null){
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        }
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
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        val id = arguments?.getInt(ARG_ID)

        val mData = GrowUpApplication.productsData[id!!]
        detailImage?.setImageResource(R.drawable.others)
        if (mData.category == "Овощи") {
            detailImage?.setImageResource(R.drawable.vegetables1)
        }
        if (mData.category == "Фрукты") {
            detailImage?.setImageResource(R.drawable.fruits1)
        }
        if (mData.category == "Животные") {
            detailImage?.setImageResource(R.drawable.animals)
        }

        GrowUpApplication.mStorage.child("UsersProfileImages").child(mData.uid).downloadUrl
            .addOnSuccessListener { task ->
                activity?.let { detailUserIcon?.let { it1 -> Glide.with(it).load(task).into(it1) } }
            }.addOnFailureListener {
                detailUserIcon?.setImageResource(R.drawable.user_icon)
            }

        detailProduct?.text = "${mData.name}, ${mData.category}, ${mData.subCategory}"
        detailUnitPrice?.text = "Цена за 1 кг: ${mData.unitPrice}"
        detailTotalPrice?.text = "Общая стоимость: ${mData.totalPrice}"
        detailSize?.text = "Объем: ${mData.size}"
        detailMessage?.text = mData.message

        GrowUpApplication.mUserRef.child(mData.uid).addValueEventListener(object : ValueEventListener{
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

        if (mData.uid!=GrowUpApplication.mAuth.currentUser?.uid){
            soldBtn?.visibility = View.GONE
        }

        whatsappBtn?.setOnClickListener {
            openWhatsApp(mData.userPhone)
        }

        dialerBtn?.setOnClickListener {
                openDialer(mData.userPhone)
        }

        detailGetUser?.setOnClickListener {
            activity?.let { it1 -> UserActivity.start(it1, mData.uid) }
        }
    }

    private fun openWhatsApp(number:String){
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

    private fun openDialer(number: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }
    companion object {
        private const val ARG_ID = "id"
        private const val ARG_KEY = "DETAIL_DATA"
        fun newInstance(id: Int): DetailDialogFragment = DetailDialogFragment().apply {
            val bundle = Bundle()
            bundle.putInt(ARG_ID, id)
            this.arguments = bundle
        }
    }

}
