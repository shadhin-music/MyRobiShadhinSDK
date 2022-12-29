package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel

internal interface DownloadedSongOnCallBack {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickFavItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onFavAlbumClick(itemPosition: Int, mSongDetails: MutableList<IMusicModel>)
}