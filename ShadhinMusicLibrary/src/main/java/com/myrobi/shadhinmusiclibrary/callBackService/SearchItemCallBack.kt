package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.search.SearchDataModel

internal interface SearchItemCallBack {
    fun onClickSearchItem(searchData: IMusicModel)
    fun onClickPlayItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickPlaySearchItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickPlayVideoItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int)
}