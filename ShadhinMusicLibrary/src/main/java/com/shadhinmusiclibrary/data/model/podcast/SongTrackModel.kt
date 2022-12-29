package com.shadhinmusiclibrary.data.model.podcast

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shadhinmusiclibrary.data.IMusicModel

// this is used for search page Song play
@Keep
internal class SongTrackModel : IMusicModel {
    @SerializedName("Id")
    override var content_Id: String = ""

    @SerializedName("ShowId")
    @Expose
    var showId: String? = null

    @SerializedName("EpisodeId")
    @Expose
    var episodeId: String? = null

    @SerializedName("ImageUrl")
    @Expose
    override var imageUrl: String? = null

    @SerializedName("Name")
    @Expose
    override var titleName: String? = null

    override var bannerImage: String? = null

    @SerializedName("ContentType")
    @Expose
    override var content_Type: String? = null

    @SerializedName("PlayUrl")
    @Expose
    override var playingUrl: String? = null

/*    @SerializedName("artistname")
    @Expose*/
//    var artistname: String? = null

    @SerializedName("Duration")
    @Expose
    override var total_duration: String? = null

//    @SerializedName("copyright")
//    @Expose
//    var copyright: String? = null

//    @SerializedName("labelname")
//    @Expose
//    var labelname: String? = null

    //    @SerializedName("releaseDate")
//    @Expose
    var releaseDate: String? = null

    @SerializedName("fav")
    @Expose
    var fav: String? = null

    //    @SerializedName("albumId")
//    @Expose
//    override var album_Id: String? = null

    //    @SerializedName("artistId")
//    @Expose
    override var artist_Id: String? = null

    @SerializedName("Starring")
    @Expose
    override var artistName: String? = null

    @SerializedName("Seekable")
    @Expose
    var seekable: Boolean? = null

    @SerializedName("Details")
    @Expose
    var details: String? = null

    @SerializedName("CeateDate")
    @Expose
    var ceateDate: String? = null

    @SerializedName("totalStream")
    @Expose
    var totalStream: Int? = null

    @SerializedName("Sort")
    @Expose
    var sort: Int? = null

    @SerializedName("TrackType")
    @Expose
    override var trackType: String? = null

    @SerializedName("IsPaid")
    @Expose
    var isPaid: Boolean? = null

    override var album_Id: String? = null
    override var album_Name: String? = null
    override var rootContentId: String? = null
    override var rootContentType: String? = null
    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = false

    fun getImageUrl300Size(): String? {
        return imageUrl?.replace("<\$size\$>", "300")
    }
}