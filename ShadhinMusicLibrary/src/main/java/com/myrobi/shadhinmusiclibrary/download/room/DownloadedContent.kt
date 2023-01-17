package com.myrobi.shadhinmusiclibrary.download.room

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.data.IMusicModel


@Keep
@Entity(tableName = "DownloadedContent")
class DownloadedContent : IMusicModel {
    @PrimaryKey
    @SerializedName("contentId")
    @Expose
    override var content_Id: String = ""

    @SerializedName("type")
    @Expose
    override var content_Type: String? = null

    @SerializedName("rootTitle")
    @Expose
    override var titleName: String? = null

    @SerializedName("rootImg")
    @Expose
    override var imageUrl: String? = null

    @SerializedName("track")
    @Expose
    override var playingUrl: String? = null

    @SerializedName("artistID")
    @Expose
    override var artist_Id: String? = null

    @SerializedName("artist")
    @Expose
    override var artistName: String? = null

    @SerializedName("timeStamp")
    @Expose
    override var total_duration: String? = null

    @SerializedName("rootId")
    @Expose
    override var rootContentId: String? = null

    @SerializedName("rootType")
    @Expose
    override var rootContentType: String? = null

    @SerializedName("isDownloaded")
    private var isDownloaded: Int? = 0

    @SerializedName("isFavorite")
    private var isFavorite: Int? = 0

    override var bannerImage: String? = null
    override var album_Id: String? = null
    override var album_Name: String? = null
    override var rootImage: String? = null
    override var isPlaying: Boolean = false
    override var isSeekAble: Boolean? = null
    override var trackType: String? = null
    fun setIsDownloaded(isDownloaded: Int) {
        this.isDownloaded = isDownloaded
    }

    fun getIsDownloaded(): Int? {
        return this.isDownloaded
    }

    fun setIsFavorite(isDownloaded: Int) {
        this.isFavorite = isDownloaded
    }

    fun getIsFavorite(): Int? {
        return this.isFavorite
    }
}