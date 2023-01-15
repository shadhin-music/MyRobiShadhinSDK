package com.myrobi.shadhinmusiclibrary.library.player

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory


internal class ShadhinExoPlayer private constructor(private val exoPlayer: ExoPlayer):ExoPlayer by exoPlayer{
    override fun seekToPrevious() {
        if(!isUserLimitOver) {
            exoPlayer.seekToPrevious()
        }
        isUserLimitOver = false
    }

    override fun seekToNext() {
        if(!isUserLimitOver) {
            exoPlayer.seekToNext()
        }
        isUserLimitOver = false
    }

    companion object{
        @JvmStatic
        public var isUserLimitOver:Boolean = false

        public  fun build(context: Context): ShadhinExoPlayer {
            val exoPlayer = ExoPlayer.Builder(context)
                .setAudioAttributes(audioAttributes(),true)
                .setHandleAudioBecomingNoisy(true)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(context)
                        .setLiveMinOffsetMs(2000)
                )
                .build()
            return ShadhinExoPlayer(exoPlayer)
        }
        private  fun audioAttributes(): AudioAttributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }
}
