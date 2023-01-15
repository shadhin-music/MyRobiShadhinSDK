package com.myrobi.shadhinmusiclibrary.data.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.download.room.WatchLaterContent

import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class VideoModel(
    @SerializedName("AlbumId")
    var albumId: String? = null,
    @SerializedName("AlbumImage")
    var albumImage: String? = null,
    @SerializedName("AlbumName")
    var albumName: String? = null,
    @SerializedName("Artist")
    var artist: String? = null,
    @SerializedName("ArtistId")
    var artistId: String? = null,
    @SerializedName("ArtistImage")
    var artistImage: String? = null,
    @SerializedName("Banner")
    var banner: String? = null,
    @SerializedName("ClientValue")
    var clientValue: Int? = null,
    @SerializedName("ContentID")
    var contentID: String? = null,
    @SerializedName("ContentType")
    var contentType: String? = null,
    @SerializedName("CreateDate")
    var createDate: String? = null,
    @SerializedName("Duration")
    var duration: String? = null,
    @SerializedName("fav")
    var fav: String? = null,
    @SerializedName("Follower")
    var follower: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("imageWeb")
    var imageWeb: String? = null,
    @SerializedName("IsPaid")
    var isPaid: Boolean? = null,
    @SerializedName("NewBanner")
    var newBanner: String? = null,
    @SerializedName("PlayCount")
    var playCount: Int? = null,
    @SerializedName("PlayListId")
    var playListId: String? = null,
    @SerializedName("PlayListImage")
    var playListImage: String? = null,
    @SerializedName("PlayListName")
    var playListName: String? = null,
    @SerializedName("PlayUrl")
    var playUrl: String? = null,
    @SerializedName("RootId")
    var rootId: String? = null,
    @SerializedName("RootType")
    var rootType: String? = null,
    @SerializedName("Seekable")
    var seekable: Boolean? = null,
    @SerializedName("TeaserUrl")
    var teaserUrl: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("TrackType")
    var trackType: String? = null,
    @SerializedName("Type")
    var type: String? = null,
    var isPlaying: Boolean = false,
    var isPlaystate: Boolean = false
) : Parcelable {

    fun setData(data: HomePatchDetailModel) {
        albumId = data.album_Id
        albumImage = data.albumImage.toString()
        albumName = data.album_Name
        artist = data.artistName
        artistId = data.artist_Id
        artistImage = data.artistImage.toString()
        banner = data.bannerImage
        clientValue = 2
        contentID = data.content_Id
        contentType = data.content_Type
        createDate = data.createDate.toString()
        duration = data.total_duration
        fav = data.fav
        follower = data.follower.toString()
        image = data.imageUrl
        imageWeb = data.imageWeb.toString()
        isPaid = data.isPaid
        newBanner = data.newBanner
        playCount = data.playCount
        playListId = data.playListId.toString()
        playListImage = data.playListImage.toString()
        playListName = data.playListName.toString()
        playUrl = data.playingUrl
        rootId = data.rootContentId
        rootType = data.rootContentType
        seekable = data.isSeekAble
        teaserUrl = data.teaserUrl
        title = data.titleName
        trackType = data.trackType
        type = data.type
    }

    fun setData2(data: LatestVideoModelDataModel) {
        albumId = data.AlbumId
        albumImage = data.AlbumImage
        albumName = data.AlbumName
        artist = data.Artist
        artistId = data.ArtistId
        artistImage = data.ArtistImage
        banner = data.Banner
        clientValue = 2
        contentID = data.ContentID
        contentType = data.ContentType
        createDate = data.CreateDate
        duration = data.Duration
        fav = data.fav
        follower = data.Follower
        image = data.image
        imageWeb = data.imageWeb
        isPaid = data.IsPaid
        newBanner = data.NewBanner
        playCount = data.PlayCount
        playListId = data.PlayListId
        playListImage = data.PlayListImage
        playListName = data.PlayListName
        playUrl = data.PlayUrl
        rootId = data.RootId
        rootType = data.RootType
        seekable = data.Seekable
        teaserUrl = data.TeaserUrl
        title = data.title
        trackType = data.TrackType
        type = data.Type
    }

    internal fun setDataDownloadIM(data: IMusicModel) {
        albumId = ""
        albumImage = ""
        albumName = ""
        artist = data.artistName
        artistId = ""
        artistImage = ""
        banner = ""
        clientValue = 2
        contentID = data.content_Id
        contentType = data.content_Type
        createDate = ""
        duration = data.total_duration
        fav = ""
        follower = ""
        image = data.imageUrl
        imageWeb = ""
        isPaid = false
        newBanner = ""
        playCount = 0
        playListId = ""
        playListImage = ""
        playListName = ""
        playUrl = data.playingUrl
        rootId = data.rootContentId
        rootType = data.rootContentType
        seekable = false
        teaserUrl = ""
        title = data.titleName
        trackType = ""
        type = data.content_Type
    }

    internal fun setDataDownload(data: DownloadedContent) {
        albumId = ""
        albumImage = ""
        albumName = ""
        artist = data.artistName
        artistId = ""
        artistImage = ""
        banner = ""
        clientValue = 2
        contentID = data.content_Id
        contentType = data.content_Type
        createDate = ""
        duration = data.total_duration
        fav = ""
        follower = ""
        image = data.imageUrl
        imageWeb = ""
        isPaid = false
        newBanner = ""
        playCount = 0
        playListId = ""
        playListImage = ""
        playListName = ""
        playUrl = data.playingUrl
        rootId = data.rootContentId
        rootType = data.rootContentType
        seekable = false
        teaserUrl = ""
        title = data.titleName
        trackType = ""
        type = data.content_Type
    }

    internal fun setDataFavorite(data: FavDataModel) {
        albumId = ""
        albumImage = ""
        albumName = ""
        artist = data.artistName
        artistId = ""
        artistImage = ""
        banner = ""
        clientValue = 2
        contentID = data.content_Id
        contentType = data.type
        createDate = ""
        duration = data.total_duration
        fav = ""
        follower = ""
        image = data.imageUrl
        imageWeb = data.imageWeb
        isPaid = false
        newBanner = ""
        playCount = 0
        playListId = ""
        playListImage = ""
        playListName = ""
        playUrl = data.playingUrl
        rootId = data.rootContentId
        rootType = data.rootContentType
        seekable = false
        teaserUrl = ""
        title = data.titleName
        trackType = ""
        type = data.type
    }

    internal fun setDataFavoriteIM(data: IMusicModel) {
        albumId = ""
        albumImage = ""
        albumName = ""
        artist = data.artistName
        artistId = ""
        artistImage = ""
        banner = ""
        clientValue = 2
        contentID = data.content_Id
        contentType = data.content_Type
        createDate = ""
        duration = data.total_duration
        fav = ""
        follower = ""
        image = data.imageUrl
        imageWeb = data.imageUrl
        isPaid = false
        newBanner = ""
        playCount = 0
        playListId = ""
        playListImage = ""
        playListName = ""
        playUrl = data.playingUrl
        rootId = data.rootContentId
        rootType = data.rootContentType
        seekable = false
        teaserUrl = ""
        title = data.titleName
        trackType = ""
        type = data.content_Type
    }

    internal fun setDataWatchlater(data: WatchLaterContent) {
        albumId = ""
        albumImage = ""
        albumName = ""
        artist = data.artist
        artistId = ""
        artistImage = ""
        banner = ""
        clientValue = 2
        contentID = data.contentId
        contentType = data.type
        createDate = ""
        duration = data.timeStamp
        fav = ""
        follower = ""
        image = data.rootImg
        imageWeb = ""
        isPaid = false
        newBanner = ""
        playCount = 0
        playListId = ""
        playListImage = ""
        playListName = ""
        playUrl = data.track
        rootId = data.rootId
        rootType = data.rootType
        seekable = false
        teaserUrl = ""
        title = data.rootTitle
        trackType = ""
        type = data.type
    }
}