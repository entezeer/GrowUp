package com.example.growup.ui.statistic

import android.view.View
import android.widget.TextView
import com.example.growup.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class ChildViewHolder(itemView: View) : ChildViewHolder(itemView) {

    var listChild: TextView

    init {
        listChild = itemView.findViewById(R.id.listChild)

    }

    fun onBind(Sousdoc: String) {
        listChild.text = Sousdoc

    }


}