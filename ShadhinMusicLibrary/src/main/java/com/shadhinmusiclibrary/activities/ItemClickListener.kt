package com.shadhinmusiclibrary.activities

import com.shadhinmusiclibrary.data.IMusicModel


internal interface ItemClickListener {
    fun onClick(position: Int, mSongDetails: IMusicModel, id: String?)
}
