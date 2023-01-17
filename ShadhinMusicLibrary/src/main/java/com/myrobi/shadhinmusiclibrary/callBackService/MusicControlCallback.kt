package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel

internal interface MusicControlCallback {
    fun playSong(mSongDetail: SongDetailModel)
}