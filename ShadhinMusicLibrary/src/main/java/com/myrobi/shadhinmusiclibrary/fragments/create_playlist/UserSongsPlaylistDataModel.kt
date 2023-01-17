package com.myrobi.shadhinmusiclibrary.fragments.create_playlist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.data.IMusicModel

@Keep
class UserSongsPlaylistDataModel() : IMusicModel {
    @SerializedName("ContentID")
    override var content_Id: String = ""

    @SerializedName("AlbumId")
    override var album_Id: String? = null

    @SerializedName("artist")
    override var artistName: String? = null

    @SerializedName("ArtistId")
    override var artist_Id: String? = null

    @SerializedName("ContentType")
    override var content_Type: String? = null

    @SerializedName("copyright")
    var copyright: String? = null

    @SerializedName("duration")
    override var total_duration: String? = null

    @SerializedName("fav")
    var fav: String? = null

    @SerializedName("image")
    override var imageUrl: String? = null

    @SerializedName("labelname")
    var labelname: String? = null

    @SerializedName("PlayUrl")
    override var playingUrl: String? = null

    @SerializedName("releaseDate")
    var releaseDate: String? = null

    @SerializedName("title")
    override var titleName: String? = null

    @SerializedName("UserPlayListId")
    var userPlayListId: String? = null

    override var bannerImage: String? = null
    override var album_Name: String?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var rootContentId: String? = null

    override var rootContentType: String? = null

    override var rootImage: String? = null

    override var isPlaying: Boolean = false

    override var isSeekAble: Boolean? = false
    override var trackType: String? = null
}