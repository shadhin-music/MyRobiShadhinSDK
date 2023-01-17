package com.myrobi.shadhinmusiclibrary.library.player

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

internal class ShadhinCustomActionReceiver(
    private val context: Context,
    var notificationManager: PlayerNotificationManager? = null
) : PlayerNotificationManager.CustomActionReceiver {
    private var isFavourite: Boolean = false
    override fun createCustomActions(
        context: Context,
        instanceId: Int
    ): MutableMap<String, NotificationCompat.Action> {
        val actionMap: MutableMap<String, NotificationCompat.Action> = HashMap()
        /*  actionMap["favourite"] = createNotificationAction("favourite", drawable.ic_favorite)
          actionMap["dislike"] = createNotificationAction("dislike", drawable.ic_favorite_white)
         */
        return actionMap
    }

    override fun getCustomActions(player: Player): MutableList<String> {
        val customActions: MutableList<String> = ArrayList()
        customActions.add(if (isFavourite) "favourite" else "dislike")
        return customActions
    }

    override fun onCustomAction(player: Player, action: String, intent: Intent) {
        when (action) {
            "favourite" -> isFavourite = false
            "dislike" -> isFavourite = true
        }
        notificationManager?.invalidate()
    }

    private fun createNotificationAction(action: String, icon: Int) = NotificationCompat.Action(
        icon, action, createPendingIntent(action)
    )

    private fun createPendingIntent(action: String): PendingIntent? = PendingIntent.getBroadcast(
        context,
        123,
        Intent(action).setPackage(context.packageName),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}