package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel

internal interface CommonBottomCallback {
    fun onClickBottomItem(mSongDetails: IMusicModel)
}