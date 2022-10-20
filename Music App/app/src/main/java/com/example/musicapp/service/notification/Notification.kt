package com.example.musicapp.service.notification

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class Notification : Application() {

    var channelID = "Lmeow"

    override fun onCreate() {
        super.onCreate()
        notification()
    }

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "Catto", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Song Notification"
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
