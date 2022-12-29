package com.shadhinmusiclibrary.utils

import androidx.recyclerview.widget.DiffUtil
import com.shadhinmusiclibrary.data.IMusicModel

class AnyTrackDiffCB() : DiffUtil.Callback() {
    private lateinit var oldSongDetails: List<IMusicModel>
    private lateinit var newSongDetails: List<IMusicModel>

    constructor(oldSongDetails: List<IMusicModel>, newSongDetails: List<IMusicModel>) : this() {
        this.oldSongDetails = oldSongDetails
        this.newSongDetails = newSongDetails
    }

    override fun getOldListSize(): Int = oldSongDetails.size

    override fun getNewListSize(): Int = newSongDetails.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldSongDetails[oldItemPosition].content_Id == newSongDetails[newItemPosition].content_Id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldSongDetails[oldItemPosition].isPlaying == newSongDetails[newItemPosition].isPlaying
}