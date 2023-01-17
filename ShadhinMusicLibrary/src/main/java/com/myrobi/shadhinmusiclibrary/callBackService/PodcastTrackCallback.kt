package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel

internal interface PodcastTrackCallback {
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickRadioItem(currentSong: IMusicModel)

}