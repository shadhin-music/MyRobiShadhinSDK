package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
internal data class FeaturedLatestTrackListDataModel(
    val contentID: String,
    val image: String,
    val title: String,
    val contentType: String,
    val playUrl: String,
    val albumId: String,
    val artistname: String,
    val duration: String,
    val copyright: String,
    val labelname: String,
    val releaseDate: String,
    val fav: Any,
    val artistId: String,
    val rootContentID: String,
    val rootImage: String,
    val rootContentType: String,
    var isPlaying: Boolean = false
) : Serializable {
    fun getImageUrl300Size(): String {
        return this.image.replace("<\$size\$>", "300")
    }
}