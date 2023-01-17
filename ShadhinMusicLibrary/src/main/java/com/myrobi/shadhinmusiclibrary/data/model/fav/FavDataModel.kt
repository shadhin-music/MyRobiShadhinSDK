package com.myrobi.shadhinmusiclibrary.data.model.fav


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.data.IMusicModel

@Keep
@Entity(tableName = "FavData")
class FavDataModel : IMusicModel {
    @PrimaryKey
    @SerializedName("ContentID")
    override var content_Id: String = ""

    @SerializedName("AlbumId")
    override var album_Id: String? = null

    @SerializedName("AlbumImage")
    var albumImage: String? = null

    @SerializedName("AlbumName")
    override var album_Name: String? = null

    @SerializedName("Artist")
    override var artistName: String? = null

    @SerializedName("ArtistId")
    override var artist_Id: String? = null

    @SerializedName("ArtistImage")
    var artistImage: String? = null

    @SerializedName("Banner")
    override var bannerImage: String? = null

    @SerializedName("ClientValue")
    var clientValue: Int? = null

    @SerializedName("ContentType")
    override var content_Type: String? = null

    @SerializedName("CreateDate")
    var createDate: String? = null

    @SerializedName("Duration")
    override var total_duration: String? = null

    @SerializedName("fav")
    var fav: String? = null

    @SerializedName("Follower")
    var follower: String? = null

    @SerializedName("image")
    override var imageUrl: String? = null

    @SerializedName("imageWeb")
    var imageWeb: String? = null

    @SerializedName("IsPaid")
    var isPaid: Boolean? = null

    @SerializedName("NewBanner")
    var newBanner: String? = null

    @SerializedName("PlayCount")
    var playCount: Int? = null

    @SerializedName("PlayListId")
    var playListId: String? = null

    @SerializedName("PlayListImage")
    var playListImage: String? = null

    @SerializedName("PlayListName")
    var playListName: String? = null

    @SerializedName("PlayUrl")
    override var playingUrl: String? = null

    @SerializedName("RootId")
    override var rootContentId: String? = null

    @SerializedName("RootType")
    override var rootContentType: String? = null

    @SerializedName("Seekable")
    var seekable: Boolean? = null

    @SerializedName("TeaserUrl")
    var teaserUrl: String? = null

    @SerializedName("title")
    override var titleName: String? = null

    @SerializedName("TrackType")
    override var trackType: String? = null

    @SerializedName("Type")
    var type: String? = null

    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = false
}