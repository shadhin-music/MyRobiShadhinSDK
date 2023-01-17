package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel

internal interface PodcastBottomSheetDialogItemCallback {
    fun onClickBottomItem(track: IMusicModel)
}