package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shadhinmusiclibrary.data.IMusicModel

/**
 * This model also used in Radio and album and other
 */

@Keep
internal class SongDetailModel : IMusicModel {
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

    @SerializedName("artist")
    @Expose
    var artistA: String? = ""

    @SerializedName("artistname")
    @Expose
    var artistB: String? = ""


    override var artistName: String? = null
        get() {
            return if(artistA.isNullOrEmpty()){
                artistB
            }else{
                artistA
            }
        }
    set(value) {
        field = value
        artistA = value
    }




    @SerializedName("duration")
    @Expose
    override var total_duration: String? = null

    @SerializedName("copyright")
    @Expose
    var copyright: String? = null

    @SerializedName("labelname")
    @Expose
    var labelname: String? = null

    @SerializedName("releaseDate")
    @Expose
    var releaseDate: String? = null

    @SerializedName("fav")
    @Expose
    var fav: String? = null

    @SerializedName("ArtistId")
    @Expose
    override var artist_Id: String? = null



    override var bannerImage: String? = null

    @SerializedName("AlbumId")
    @Expose
    override var album_Id: String? = null

    override var album_Name: String? = null
    override var rootContentId: String? = null
    override var rootContentType: String? = null
    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = false
    override var trackType: String? = null
}