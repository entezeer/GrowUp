package com.example.growup.ui.statistic

import android.content.Context
import android.widget.Toast
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.growup.R
import com.example.growup.data.statistic.model.ParentList
import com.example.growup.ui.statistic.viewholder.ChildViewHolder
import com.example.growup.ui.statistic.viewholder.ParentViewHolder
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter


class ExpandableRecyclerAdapter(groups: List<ParentList>, var context: Context, var listener: Listener) :
    ExpandableRecyclerViewAdapter<ParentViewHolder, ChildViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_parent, parent, false)
        return ParentViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindChildViewHolder(
        holder: ChildViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {

        val childItem = (group as ParentList).items[childIndex]
        childItem.title?.let { holder.onBind(it) }
        val TitleChild = group.getTitle()
        holder.itemView.setOnClickListener {
            listener.onItemSelectedAt(TitleChild,childItem.title)
            val toast = Toast.makeText(context, TitleChild, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onBindGroupViewHolder(holder: ParentViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setParentTitle(group)

        if (group.items == null) {
            holder.listGroup.setOnClickListener {
                val toast = Toast.makeText(context, group.toString(), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    interface Listener {
        fun onItemSelectedAt(key: String, childKey: String?)
    }
}


