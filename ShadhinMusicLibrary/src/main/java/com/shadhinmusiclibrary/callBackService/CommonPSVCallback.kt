package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

internal interface CommonPSVCallback {
    fun onClickBottomItemPodcast(mSongDetails: IMusicModel)
    fun onClickBottomItemSongs(mSongDetails: IMusicModel)
    fun onClickBottomItemVideo(mSongDetails: IMusicModel)
}