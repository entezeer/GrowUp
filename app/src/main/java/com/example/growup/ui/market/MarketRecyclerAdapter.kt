package com.example.growup.ui.market

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.growup.R
import com.example.growup.data.market.model.Products
import java.util.*
import kotlin.collections.ArrayList


class MarketRecyclerAdapter(private val items: ArrayList<Products>, var listener: Listener, var context: Context) :
    RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder>() {
    private var arrayList: ArrayList<Products> = ArrayList()
    init {
        arrayList.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.market_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerImage = itemView.findViewById<ImageView>(R.id.product_image)
        private val recyclerTitle = itemView.findViewById<TextView>(R.id.product_title)
        private val recyclerPrice = itemView.findViewById<TextView>(R.id.product_price)

        fun bindData(items: Products) {
            recyclerImage.setImageResource(R.drawable.others)
            if (items.category == "Овощи") {
                recyclerImage.setImageResource(R.drawable.vegetables2)
            }
            if (items.category == "Фрукты") {
                recyclerImage.setImageResource(R.drawable.fruits2)
            }
            if (items.category == "Животные") {
                recyclerImage.setImageResource(R.drawable.animals1)
            }

            recyclerTitle.text = items.subCategory
            recyclerPrice.text = items.unitPrice

            itemView.setOnClickListener {
                listener.onItemSelectedAt(adapterPosition)
            }
        }
    }

    fun filter(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        items.clear()
        if (text.isEmpty()) {
            items.addAll(arrayList)
        } else {
            for (wp in arrayList) {
                if (wp.subCategory.toLowerCase(Locale.getDefault()).contains(text)) {
                    items.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemSelectedAt(position: Int)
    }


}