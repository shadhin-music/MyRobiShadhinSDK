package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

internal interface CommonBottomCallback {
    fun onClickBottomItem(mSongDetails: IMusicModel)
}