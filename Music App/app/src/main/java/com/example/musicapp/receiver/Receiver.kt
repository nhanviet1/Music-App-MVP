package com.example.musicapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.musicapp.service.BoundService
import com.example.musicapp.view.ACTION_NEXT
import com.example.musicapp.view.ACTION_PAUSE
import com.example.musicapp.view.ACTION_PLAY
import com.example.musicapp.view.ACTION_PREVIOUS


class Receiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val actionMusic = p1?.getIntExtra("actionMusic", 0)
//        val intentService = Intent(p0, BoundService::class.java)
//        intentService.putExtra("actionMusic", actionMusic)
//        p0?.startService(intentService)
        Log.d("Lmeow", "Receiver actionMusic: $actionMusic")
        when (actionMusic) {
            0 -> {
                Intent(ACTION_PAUSE).run {
                    putExtra("actionMusic", "$actionMusic")
                    p0!!.sendBroadcast(this)
                }
            }
            1 -> {
                Intent(ACTION_PLAY).run {
                    putExtra("actionMusic", "$actionMusic")
                    p0!!.sendBroadcast(this)
                }
            }
            2 -> {
                Intent(ACTION_NEXT).run {
                    putExtra("actionMusic", "$actionMusic")
                    p0!!.sendBroadcast(this)
                }
            }
            3 -> {
                Intent(ACTION_PREVIOUS).run {
                    putExtra("actionMusic", "$actionMusic")
                    p0!!.sendBroadcast(this)
                }
            }
        }

    }
}