package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel

internal interface CommonPSVCallback {
    fun onClickBottomItemPodcast(mSongDetails: IMusicModel)
    fun onClickBottomItemSongs(mSongDetails: IMusicModel)
    fun onClickBottomItemVideo(mSongDetails: IMusicModel)
}