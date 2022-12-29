package com.shadhinmusiclibrary.callBackService

import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.data.IMusicModel

internal interface RadioTrackCallBack {
    fun onClickItem(currentSong: IMusicModel)
    fun getCurrentVH(currentVH: RecyclerView.ViewHolder, songDetails: MutableList<IMusicModel>)
}