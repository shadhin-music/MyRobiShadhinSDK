package com.myrobi.shadhinmusiclibrary.callBackService

import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.data.IMusicModel

internal interface CommonPlayControlCallback {
    fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int)
    fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>
    )
}