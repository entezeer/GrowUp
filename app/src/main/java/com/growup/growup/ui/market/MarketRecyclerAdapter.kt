package com.growup.growup.ui.market

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.growup.growup.R
import com.growup.growup.data.market.model.Products
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
            if (items.productImage.isEmpty()) {
                when (items.category) {
                    "Овощи" -> recyclerImage.setImageResource(R.drawable.vegetables3)
                    "Фрукты" -> recyclerImage.setImageResource(R.drawable.fruits2)
                    "Животные" -> recyclerImage.setImageResource(R.drawable.animals1)
                    "Другое" -> recyclerImage.setImageResource(R.drawable.others)
                }
            } else {
                Glide.with(context).load(Uri.parse(items.productImage))
                    .placeholder(R.drawable.splash)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .onlyRetrieveFromCache(true)
                    .apply(RequestOptions().override(600, 1000))
                    .into(recyclerImage)
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

    fun getSortData(productLocation: String, productCategory: String, productSubCategory: String) {
        items.clear()
        when {
            productLocation.isEmpty() -> for (wp in arrayList) {
                if (wp.category.trim().equals(productCategory.trim()) &&
                    wp.subCategory.trim().equals(productSubCategory.trim())
                ) {
                    items.add(wp)
                }
            }
            productCategory.isEmpty() && productSubCategory.isEmpty() -> for (wp in arrayList) {
                if (wp.location.substringBefore(',').trim().equals(productLocation.trim())
                ) {
                    items.add(wp)
                }
            }

            else -> for (wp in arrayList) {
                if (wp.location.substringBefore(',').trim().equals(productLocation.trim()) &&
                    wp.category.trim().equals(productCategory.trim()) &&
                    wp.subCategory.trim().equals(productSubCategory.trim())
                ) {
                    items.add(wp)
                }
            }
        }

    }


    interface Listener {
        fun onItemSelectedAt(position: Int)
    }
}

