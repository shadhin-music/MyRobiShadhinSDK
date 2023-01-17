package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel

interface CommonSingleCallback {
    fun onClickItem(currentSong: IMusicModel, clickItemPosition: Int)
}