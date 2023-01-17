package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel

internal interface LatestReleaseOnCallBack {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
}