package com.myrobi.shadhinmusiclibrary.library.player.utils.convater

import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music

internal interface MusicConverter {
    fun convert(): Music
}