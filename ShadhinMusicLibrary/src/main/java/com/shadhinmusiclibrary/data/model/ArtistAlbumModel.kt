package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData
import java.io.Serializable

@Keep
internal data class ArtistAlbumModel(
    val `data`: MutableList<ArtistAlbumModelData>,
    val fav: String,
    val follow: String,
    val image: String,
    val isPaid: Boolean,
    val message: String,
    val status: String,
    val type: String
) : Serializable