package com.shadhinmusiclibrary.fragments.create_playlist


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class PlaylistContent(
    @SerializedName("AlbumId")
    var albumId: Any?,
    @SerializedName("artist")
    var artist: String?,
    @SerializedName("ArtistId")
    var artistId: Any?,
    @SerializedName("ContentID")
    var contentID: String?,
    @SerializedName("ContentType")
    var contentType: String?,
    @SerializedName("copyright")
    var copyright: String?,
    @SerializedName("duration")
    var duration: String?,
    @SerializedName("fav")
    var fav: String?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("labelname")
    var labelname: String?,
    @SerializedName("PlayUrl")
    var playUrl: String?,
    @SerializedName("releaseDate")
    var releaseDate: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("UserPlayListId")
    var userPlayListId: Any?
):Serializable