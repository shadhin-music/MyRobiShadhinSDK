package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.model.SongDetailModel

internal interface MusicControlCallback {
    fun playSong(mSongDetail: SongDetailModel)
}