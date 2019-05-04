package com.example.growup.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.growup.R
import kotlinx.android.synthetic.main.lang_spinner_item.view.*

class LangSpinnerAdapter(val context:Context, val country: Array<String>, val flag: Array<Int>) : BaseAdapter() {
    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = mInflater.inflate(R.layout.lang_spinner_item, parent, false)
        var countryName: TextView = convertView.findViewById(R.id.country_name) as TextView
        var countryFlag: ImageView = convertView.findViewById(R.id.country_flag) as ImageView

        countryName.text =  country[position]
        countryFlag.setImageResource(flag[position])
        return convertView
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return country.size
    }


}


