package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

internal interface PodcastTrackCallback {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
}