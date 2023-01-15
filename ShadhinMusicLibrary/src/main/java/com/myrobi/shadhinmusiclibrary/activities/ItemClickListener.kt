package com.myrobi.shadhinmusiclibrary.activities

import com.myrobi.shadhinmusiclibrary.data.IMusicModel


internal interface ItemClickListener {
    fun onClick(position: Int, mSongDetails: IMusicModel, id: String?)
}
