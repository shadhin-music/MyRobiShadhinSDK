package com.myrobi.shadhinmusiclibrary.library.player.data.source

import com.google.android.exoplayer2.source.MediaSource

interface MediaSourceBuilder {
    fun build():MediaSource
}