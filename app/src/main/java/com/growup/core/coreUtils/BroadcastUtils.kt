package com.growup.core.coreUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

object BroadcastUtils {
    const val EXTRA_LAT = "lat"
    const val EXTRA_LNG = "lng"
    const val EXTRA_MESSAGE = "message"
    const val EXTRA_LOCATION_OFF = "Location off"

    const val BROADCAST_ACTION = "ru.entezeer.weatherdarksky.ON_LOCATION_UPDATE"

    fun register(locationReceiver: BroadcastReceiver, context: Context) {
        context.registerReceiver(locationReceiver, IntentFilter(BROADCAST_ACTION))
    }

    fun unRegister(locationReceiver: BroadcastReceiver, context: Context) {
        context.unregisterReceiver(locationReceiver)
    }

    fun sendLocation(context: Context, lat: Double, lng: Double) {
        val intent = Intent(BROADCAST_ACTION)
        intent.putExtra(EXTRA_LAT, lat)
        intent.putExtra(EXTRA_LNG, lng)
        context.sendBroadcast(intent)
    }

    fun sendMessage(context: Context, message: String) {
        val intent = Intent(BROADCAST_ACTION)
        intent.putExtra(EXTRA_MESSAGE, message)
        context.sendBroadcast(intent)
    }

}