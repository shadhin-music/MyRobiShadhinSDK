package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel

internal interface DownloadedSongOnCallBack {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickFavItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onFavAlbumClick(itemPosition: Int, mSongDetails: MutableList<IMusicModel>)
}