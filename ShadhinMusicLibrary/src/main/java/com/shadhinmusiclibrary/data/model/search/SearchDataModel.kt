package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.shadhinmusiclibrary.data.IMusicModel


@Keep
internal class SearchDataModel : IMusicModel {
    @SerializedName("ContentID")
    override var content_Id: String = ""

    @SerializedName("image")
    override var imageUrl: String? = null

    @SerializedName("imageWeb")
    var imageWeb: String? = null

    @SerializedName("title")
    override var titleName: String? = null

    @SerializedName("ContentType")
    override var content_Type: String? = null

    @SerializedName("PlayUrl")
    override var playingUrl: String? = null

    @SerializedName("Duration")
    override var total_duration: String? = null

    @SerializedName("fav")
    var fav: String? = null

    @SerializedName("Banner")
    override var bannerImage: String? = null

    @SerializedName("NewBanner")
    var newBanner: String? = null

    @SerializedName("PlayCount")
    var playCount: Int? = null

    @SerializedName("Type")
    var type: String? = null

    @SerializedName("IsPaid")
    var isPaid: Boolean? = null

    @SerializedName("Seekable")
    var seekable: Boolean? = null

    @SerializedName("TrackType")
    override var trackType: String? = null

    @SerializedName("ArtistId")
    override var artist_Id: String? = null

    @SerializedName("Artist")
    override var artistName: String? = null

    @SerializedName("ArtistImage")
    var artistImage: String? = null

    @SerializedName("AlbumId")
    override var album_Id: String? = null

    @SerializedName("AlbumName")
    override var album_Name: String? = null

    @SerializedName("AlbumImage")
    var albumImage: String? = null

    @SerializedName("PlayListId")
    var playListId: String? = null

    @SerializedName("PlayListName")
    var playListName: String? = null

    @SerializedName("PlayListImage")
    var playListImage: String? = null

    @SerializedName("CreateDate")
    var createDate: String? = null

    @SerializedName("RootId")
    override var rootContentId: String? = null

    @SerializedName("RootType")
    override var rootContentType: String? = null

    @SerializedName("TeaserUrl")
    var teaserUrl: String? = null

    @SerializedName("Follower")
    var follower: String? = null

    @SerializedName("ClientValue")
    var clientValue: Int? = null

    override var rootImage: String? = null

    override var isPlaying: Boolean = false

    override var isSeekAble: Boolean? = false
}