package com.example.growup.ui.market

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.growup.R
import com.example.growup.models.Products
import java.util.ArrayList

class MarketRecyclerAdapter(private val items: ArrayList<Products>, var listener: Listener, var context: Context) :
    RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder>() {

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

        fun bindData(items: Products) {
            if (items.category == "vegetables") {
                recyclerImage.setImageResource(R.drawable.vegetables)
            }
            if (items.category == "fruits") {
                recyclerImage.setImageResource(R.drawable.fruits)
            }
            if (items.category == "animals") {
                recyclerImage.setImageResource(R.drawable.animals)
            }

            recyclerTitle.text = items.subCategory

            itemView.setOnClickListener {
                listener.onItemSelectedAt(adapterPosition)
            }
        }
    }

    interface Listener {
        fun onItemSelectedAt(position: Int)
    }


}