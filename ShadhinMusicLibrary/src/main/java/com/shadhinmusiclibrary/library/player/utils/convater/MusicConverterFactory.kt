package com.shadhinmusiclibrary.library.player.utils.convater

import com.shadhinmusiclibrary.data.model.ArtistContentDataModel
import com.shadhinmusiclibrary.library.player.data.model.Music

private class MusicConverterFactory private constructor(private val obj: Any) {
    private var converter: MusicConverter? = null

    init {
        initialization()
    }

    private fun initialization() {
        converter = when (obj) {
            is ArtistContentDataModel -> ArtistContentToMusic(obj)
            else -> null
        }
    }

    fun convert(): Music {
        return converter!!.convert()
    }

    companion object {
        fun Any.toMusic(): Music {
            return MusicConverterFactory(this).convert()
        }
    }
}