package com.myrobi.shadhinmusiclibrary.library.player

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.myrobi.shadhinmusiclibrary.library.player.Constants.MUSIC_NOTIFICATION_CHANNEL_ID
import com.myrobi.shadhinmusiclibrary.library.player.Constants.MUSIC_NOTIFICATION_ID
import com.myrobi.shadhinmusiclibrary.library.player.utils.getPreloadBitmap
import com.myrobi.shadhinmusiclibrary.library.player.utils.nullFix
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.myrobi.shadhinmusiclibrary.R

internal class ShadhinMusicNotificationManager(
    private val context: Context,
    private val token: MediaSessionCompat.Token?,
    private val notificationListener: PlayerNotificationManager.NotificationListener?
) {

    private lateinit var notificationManager: PlayerNotificationManager
    private var shadhinCustomActionReceiver: ShadhinCustomActionReceiver =
        ShadhinCustomActionReceiver(context)
    private var placeholderArtwork: Bitmap? =
        defaultArtwork()// this placeholder only for fix samsung one UI blink issue
    private var mediaControllerCompat: MediaControllerCompat? = null

    init {
        mediaControllerCompat = token?.let { MediaControllerCompat(context, it) }
        notificationManager = PlayerNotificationManager.Builder(
            context,
            MUSIC_NOTIFICATION_ID,
            MUSIC_NOTIFICATION_CHANNEL_ID
        ).apply {
            setMediaDescriptionAdapter(MusicMediaDescriptionAdapter())
            notificationListener?.let { setNotificationListener(it) }

        }
            .build().apply {
                setSmallIcon(R.drawable.my_bl_sdk_ic_shadhin_icon_gray_vector)
                token?.let { setMediaSessionToken(it) }
            }
    }

    fun showNotification(player: Player?) = notificationManager.setPlayer(player)
    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    inner class MusicMediaDescriptionAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaControllerCompat?.metadata?.description?.title.nullFix()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaControllerCompat?.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaControllerCompat?.metadata?.description?.subtitle.nullFix()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val uri: Uri? = mediaControllerCompat?.metadata?.description?.iconUri
            val mediaId = mediaControllerCompat?.metadata?.description?.mediaId

            /* bitmapFromUri(context,uri){ bitmap->
                 if(bitmap !=null){
                     callback.onBitmap(bitmap)
                 }
             }*/

            if (uri != null) {

                Glide.with(context).asBitmap()
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                        ) {
                            // onBitmap(resource)
                            callback.onBitmap(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }


                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            //onBitmap(null)
                            //callback.onBitmap(null)
                        }
                    })
            }


            return mediaId?.let { getPreloadBitmap(it) }
        }

    }

    private fun defaultArtwork(): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, R.drawable.my_bl_sdk_default_song)
    }
}