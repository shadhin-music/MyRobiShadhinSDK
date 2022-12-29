package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.search.SearchDataModel

internal interface SearchItemCallBack {
    fun onClickSearchItem(searchData: IMusicModel)
    fun onClickPlayItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickPlaySearchItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickPlayVideoItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
}