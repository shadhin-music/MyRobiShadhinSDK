package com.myrobi.shadhinmusiclibrary.library.player.data.rest

import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music


internal interface MusicRepository {
    fun fetchURL(music: Music): String
    fun refreshStreamingStatus()
    fun fetchDownloadedURL(name: String): String
}