package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

internal interface LatestReleaseOnCallBack {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
}