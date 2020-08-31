package com.matthew.inspections.util

import android.content.Context


object Util {

    fun updateNetworkStuffs(context: Context, network: Boolean) {
        //Network has gone down or up
        //when offline stop job queue
        //when online start job queue to start sending data to server again
    }
}