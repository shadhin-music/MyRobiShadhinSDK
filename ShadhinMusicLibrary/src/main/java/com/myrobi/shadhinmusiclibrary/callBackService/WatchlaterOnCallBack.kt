package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.download.room.WatchLaterContent

internal interface WatchlaterOnCallBack {
    fun onClickItem(mSongDetails: MutableList<WatchLaterContent>, clickItemPosition: Int)
}