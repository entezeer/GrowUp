package com.entezeer.tracking.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.growup.R

object InternetUtil {
    @SuppressLint("NewApi")
    fun checkInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null){
            val info = connectivityManager.activeNetworkInfo
            if (info != null){
                if (info.state == NetworkInfo.State.CONNECTED){
                    return true
                }
            }
        }
        return false
    }

    fun showInternetAlert(context: Context){

        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.network_alert_dialog,null)
        val gifImageView = view.findViewById<ImageView>(R.id.gif_image_view)


        Glide.with(context).load(R.drawable.nointernet).into(gifImageView)

        val dialog = AlertDialog.Builder(context)
        dialog.setPositiveButton("Попробовать снова"){_,_->

        }
        dialog.setView(view)
        dialog.show()
    }
}