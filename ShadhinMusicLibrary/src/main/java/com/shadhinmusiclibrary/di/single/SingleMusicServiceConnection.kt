package com.shadhinmusiclibrary.di.single

import android.content.Context
import com.shadhinmusiclibrary.library.player.connection.MusicServiceController
import com.shadhinmusiclibrary.library.player.connection.ShadhinMusicServiceConnection

internal class SingleMusicServiceConnection private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: MusicServiceController? = null
        fun getInstance(context: Context): MusicServiceController =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance(context).also { INSTANCE = it }
            }

        private fun newInstance(context: Context): MusicServiceController {
            return ShadhinMusicServiceConnection(context)
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}