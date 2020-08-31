package com.matthew.inspections.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

//wifi receiver detects when wifi turns on or off
class NetworkReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_WIFI = 1
        const val TYPE_MOBILE = 2
        const val TYPE_NOT_CONNECTED = 0
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Util.updateNetworkStuffs(context, getConnectivityStatus(context))
    }

    private fun connectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    private fun getConnectivityStatus(context: Context): Boolean =
        when(connectivityStatus(context)){
            TYPE_WIFI -> true
            TYPE_MOBILE -> true
            TYPE_NOT_CONNECTED -> false
            else -> false
        }


}