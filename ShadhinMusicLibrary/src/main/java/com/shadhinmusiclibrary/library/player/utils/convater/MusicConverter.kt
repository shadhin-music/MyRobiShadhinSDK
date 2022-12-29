package com.shadhinmusiclibrary.library.player.utils.convater

import com.shadhinmusiclibrary.library.player.data.model.Music

internal interface MusicConverter {
    fun convert(): Music
}