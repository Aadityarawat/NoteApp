package com.example.noteapp.others

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkCheck {
    fun isNetworkActive(context : Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val information: Array<NetworkInfo> = connectivityManager.allNetworkInfo
            for (info in information) {
                if (info.state == NetworkInfo.State.CONNECTED) return true
            }
        }
        return false

    }
}