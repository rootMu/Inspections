package com.matthew.inspections.util

import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import androidx.annotation.NonNull
import com.firebase.jobdispatcher.JobService

class ConnectivityJob : JobService() {

    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = object : NetworkCallback() {
        override fun onAvailable(@NonNull network: Network) {
            super.onAvailable(network)
            //available
        }

        override fun onLost(@NonNull network: Network) {
            super.onLost(network)
            //lost
        }

        override fun onUnavailable() {
            super.onUnavailable()
            //unavailable
        }
    }


    override fun onStartJob(job: com.firebase.jobdispatcher.JobParameters): Boolean {

        return try {
            connectivityManager.registerDefaultNetworkCallback(callback)

            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(), callback
            )

            true
        } catch (e: Exception) {
            //isNetworkConnected = false
            false
        }
    }

    override fun onStopJob(job: com.firebase.jobdispatcher.JobParameters): Boolean {
        connectivityManager.unregisterNetworkCallback(
            callback
        )
        return true
    }

}