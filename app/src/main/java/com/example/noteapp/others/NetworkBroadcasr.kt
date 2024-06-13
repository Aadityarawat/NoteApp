package com.example.noteapp.others

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import com.example.noteapp.R

class NetworkBroadcasr : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val networkCheck = NetworkCheck()

        if (context?.let { networkCheck.isNetworkActive(it) } == false){

            val builder = AlertDialog.Builder(context)
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.network_dialog, null)
            builder.setView(dialogLayout)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            dialog.setCancelable(false)
            dialog.window?.setGravity(Gravity.CENTER)
            dialog.show()

            val retrybtn = dialogLayout.findViewById<Button>(R.id.retry)

            retrybtn.setOnClickListener {
                dialog.dismiss()
                onReceive(context,intent)
            }
        }
    }
}