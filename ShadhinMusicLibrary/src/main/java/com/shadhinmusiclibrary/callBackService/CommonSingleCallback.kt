package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

interface CommonSingleCallback {
    fun onClickItem(currentSong: IMusicModel, clickItemPosition: Int)
}