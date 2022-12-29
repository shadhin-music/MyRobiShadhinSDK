package com.shadhinmusiclibrary.library.player.listener

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.shadhinmusiclibrary.library.player.Constants.MUSIC_NOTIFICATION_ID
import com.shadhinmusiclibrary.library.player.ShadhinMusicPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.shadhinmusiclibrary.utils.exH

internal class ShadhinMusicPlayerNotificationListener(
    private val musicService: ShadhinMusicPlayer
) : PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        musicService.apply {
            exH {
                stopForeground(true)
                isForegroundService = false
                stopSelf()
            }

        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {

        musicService.apply {

            if (ongoing && !isForegroundService) {
               exH {
                   ContextCompat.startForegroundService(
                       this,
                       Intent(applicationContext, this::class.java)
                   )
                   startForeground(MUSIC_NOTIFICATION_ID, notification)
                   isForegroundService = true
               }
            }
        }
    }
}