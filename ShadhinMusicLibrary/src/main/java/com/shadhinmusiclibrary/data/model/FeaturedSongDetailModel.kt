package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.shadhinmusiclibrary.data.IMusicModel

@Keep
internal class FeaturedSongDetailModel : IMusicModel {
    @SerializedName("contentID")
    override var content_Id: String = ""

    @SerializedName("image")
    override var imageUrl: String? = null

    @SerializedName("title")
    override var titleName: String? = null

    @SerializedName("contentType")
    override var content_Type: String? = null

    @SerializedName("playUrl")
    override var playingUrl: String? = null

    @SerializedName("albumId")
    override var album_Id: String? = null

    @SerializedName("artistname")
    override var artistName: String? = null

    @SerializedName("duration")
    override var total_duration: String? = null

    @SerializedName("copyright")
    var copyright: String? = null

    @SerializedName("labelname")
    var labelname: String? = null

    @SerializedName("releaseDate")
    var releaseDate: String? = null

    @SerializedName("fav")
    var fav: Any? = null

    @SerializedName("artistId")
    override var artist_Id: String? = null
    override var trackType: String? = null
    override var bannerImage: String? = null
    override var album_Name: String? = null
    override var rootContentId: String? = null
    override var rootContentType: String? = null
    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = false
}