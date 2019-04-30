package com.example.growup.ui.statistic

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.growup.R
import com.example.growup.models.Regions
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class CategoryAdapter(
    private val items: ArrayList<String>,
    var context: Context
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindData(items[position],position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title = itemView.findViewById<TextView>(R.id.category_title)
        private var category = itemView.findViewById<LinearLayout>(R.id.category)
        private var subRecyclerView = itemView.findViewById<RecyclerView>(R.id.sub_category_recycler)


        fun bindData(items: String, position: Int) {
            title.text = items
            subRecyclerView.visibility = View.GONE
            subRecyclerView.layoutManager = LinearLayoutManager(context)
            subRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
            subRecyclerView.adapter = SubCategoryAdapter(context, Regions.regionsList[position],object : SubCategoryAdapter.Listener{
                override fun onSubItemSelectedAt(position: Int) {

                }
            })
            if (position == selectedPosition){
                subRecyclerView.visibility = View.VISIBLE
                category.background = ContextCompat.getDrawable(context, R.drawable.green_round_back)
            }
            else {
                subRecyclerView.visibility = View.GONE
                category.background = ContextCompat.getDrawable(context, R.drawable.border_button)
            }
            category.setOnClickListener {
                if (selectedPosition != position){
                    selectedPosition = position
                    notifyDataSetChanged()
                }
                else{
                    selectedPosition = -1
                    notifyDataSetChanged()
                }
            }
        }

    }

}