package com.myrobi.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.data.IMusicModel


@Keep
internal class TopTrendingDataModel : IMusicModel {
    @SerializedName("ContentID")
    @Expose
    override var content_Id: String = ""

    @SerializedName("image")
    @Expose
    override var imageUrl: String? = null

    @SerializedName("title")
    @Expose
    override var titleName: String? = null

    @SerializedName("ContentType")
    @Expose
    override var content_Type: String? = null

    @SerializedName("PlayUrl")
    @Expose
    override var playingUrl: String? = null

    @SerializedName("artistname")
    @Expose
    override var artistName: String? = null

    @SerializedName("duration")
    @Expose
    override var total_duration: String? = null

    @SerializedName("copyright")
    @Expose
    var copyright: Any? = null

    @SerializedName("labelname")
    @Expose
    var labelname: String? = null

    @SerializedName("releaseDate")
    @Expose
    var releaseDate: Any? = null

    @SerializedName("fav")
    @Expose
    var fav: Any? = null

    @SerializedName("AlbumId")
    @Expose
    override var album_Id: String? = null

    @SerializedName("ArtistId")
    @Expose
    override var artist_Id: String? = null

    @SerializedName("TotalStream")
    @Expose
    var totalStream: String? = null

    override var bannerImage: String? = null
    override var album_Name: String? = null
    override var rootContentId: String? = null
    override var rootContentType: String? = null
    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = false
    override var trackType: String? = null
}