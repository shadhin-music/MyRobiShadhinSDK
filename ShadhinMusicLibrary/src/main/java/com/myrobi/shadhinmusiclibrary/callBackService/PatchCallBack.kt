package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.model.PodcastDetailsModel

internal interface PatchCallBack {
    fun onClickItemAndAllItem(itemPosition: Int, selectedData: List<PodcastDetailsModel>)
}