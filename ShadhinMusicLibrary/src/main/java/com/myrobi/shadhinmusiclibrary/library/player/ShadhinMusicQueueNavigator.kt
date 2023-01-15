package com.myrobi.shadhinmusiclibrary.library.player

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.myrobi.shadhinmusiclibrary.library.player.utils.toServiceMediaItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator


internal class ShadhinMusicQueueNavigator(sessionCompat: MediaSessionCompat) :
    TimelineQueueNavigator(sessionCompat) {
    override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {

        musicIndex = windowIndex
        val playerItem =
            (player as ExoPlayer)
                .getMediaItemAt(windowIndex)
                .toServiceMediaItem()
        return playerItem.description
    }

    companion object {
        var musicIndex: Int = 0
    }
}