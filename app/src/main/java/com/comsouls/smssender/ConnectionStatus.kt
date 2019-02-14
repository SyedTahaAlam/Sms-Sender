package com.comsouls.smssender

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

/**
 * Created by Hamdard Jee on 3/29/2018.
 */

object ConnectionStatus {

    fun checkNetworkConnection(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No internet Connection")
        builder.setMessage("Please turn on internet connection to continue")
        builder.setNegativeButton("close") { dialog, which -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun isNetworkConnectionAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnected
        if (isConnected) {
            Log.d("Network", "Connected")
            return true
        } else {
            checkNetworkConnection(context)
            Log.d("Network", "Not Connected")
            return false
        }
    }
}
