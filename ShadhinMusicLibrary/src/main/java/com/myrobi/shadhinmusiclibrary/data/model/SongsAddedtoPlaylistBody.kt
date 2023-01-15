package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
data class SongsAddedtoPlaylistBody(val playlistId:String?= null, val contentId:String?=null)
