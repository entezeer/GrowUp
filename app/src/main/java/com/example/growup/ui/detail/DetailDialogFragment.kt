package com.example.growup.ui.detail


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.growup.GrowUpApplication
import com.example.growup.R
import android.widget.Toast
import android.content.pm.PackageManager
import android.net.Uri


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
    private var whatsappBtn: Button? = null
    private var dialerBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_dialog, container, false)

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
        whatsappBtn = view.findViewById(R.id.whatsapp_btn)
        dialerBtn = view.findViewById(R.id.call_btn)
        detailMessage = view.findViewById(R.id.detail_message)
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
        detailProduct?.text = "${mData.name}, ${mData.category}, ${mData.subCategory}"
        detailUnitPrice?.text = "Цена за 1 кг: ${mData.unitPrice}"
        detailTotalPrice?.text = "Общая стоимость: ${mData.totalPrice}"
        detailSize?.text = "Объем: ${mData.size}"
        detailUserName?.text = "Продавец: ${mData.user}"
        detailUserPhone?.text = "Телефон: ${mData.userPhone}"
        detailLocation?.text = "Местоположение: ${mData.location}"
        detailMessage?.text = mData.message

        whatsappBtn?.setOnClickListener {
            openWhatsApp(mData.userPhone)
        }

        dialerBtn?.setOnClickListener {
                openDialer(mData.userPhone)
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
