package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel

internal interface PodcastBottomSheetDialogItemCallback {
    fun onClickBottomItem(track: IMusicModel)
}