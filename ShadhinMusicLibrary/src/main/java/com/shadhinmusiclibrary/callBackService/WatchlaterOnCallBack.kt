package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.download.room.WatchLaterContent

internal interface WatchlaterOnCallBack {
    fun onClickItem(mSongDetails: MutableList<WatchLaterContent>, clickItemPosition: Int)
}