package com.entezeer.tracking.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

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
}