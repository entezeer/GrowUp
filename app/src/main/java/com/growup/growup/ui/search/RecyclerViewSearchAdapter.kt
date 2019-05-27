package com.growup.growup.ui.search

import android.content.Context

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.growup.growup.GrowUpApplication
import com.growup.growup.R
import com.growup.growup.data.user.model.User
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewSearchAdapter(private val users: ArrayList<User>, private val userImages: ArrayList<String>,
                                var listener: Listener, var context: Context ) :
        RecyclerView.Adapter<RecyclerViewSearchAdapter.ViewHolder>(){

    private var usersList: ArrayList<User> = ArrayList()
    init {
        usersList.addAll(users)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_search_item,p0,false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindData(users[i] , userImages[i])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val userImage = itemView.findViewById<ImageView>(R.id.user_image_list)
        private val userNameSurname= itemView.findViewById<TextView>(R.id.user_name_surname)
        private val userType= itemView.findViewById<TextView>(R.id.user_type)

        fun bindData(data: User, imagePath: String) {
            GrowUpApplication.mStorage.child("UsersProfileImages").child(imagePath)
                .downloadUrl
                .addOnSuccessListener { task ->
                    Glide.with(context).load(task).into(userImage!!)
                }.addOnFailureListener {
                    userImage?.setImageResource(R.drawable.user_icon)
                }

            userNameSurname.text = data.name + " " + data.lastName
            userType.text = data.userType
            itemView.setOnClickListener{
                listener.onItemSelectedAt(adapterPosition)
            }
        }
    }

    fun filter(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        users.clear()
        if (text.isEmpty()) {
            users.addAll(usersList)
        } else {
            for (u in usersList) {
                if ((u.name.toLowerCase(Locale.getDefault()) + " " + u.name.toLowerCase(Locale.getDefault())).contains(text)) {
                    users.add(u)
                }
            }
        }
        notifyDataSetChanged()
    }
    interface Listener {
        fun onItemSelectedAt(position: Int)
    }
}


