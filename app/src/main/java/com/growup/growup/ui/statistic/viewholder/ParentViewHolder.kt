package com.growup.growup.ui.statistic.viewholder

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.growup.growup.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation

class ParentViewHolder(itemView: View) : GroupViewHolder(itemView) {

    var listGroup: TextView
    var arrow: ImageView

    init {
        listGroup = itemView.findViewById(R.id.listParent)
        arrow = itemView.findViewById(R.id.arrow)
    }

    fun setParentTitle(group: ExpandableGroup<*>) {
        listGroup.text = group.title
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }


}