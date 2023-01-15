package com.myrobi.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class LastFmArtistDataModel {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("mbid")
    @Expose
    var mbid: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("image")
    @Expose
    var image: List<LastFmImageDataModel>? = null

    @SerializedName("streamable")
    @Expose
    var streamable: String? = null

    @SerializedName("ontour")
    @Expose
    var ontour: String? = null

    @SerializedName("stats")
    @Expose
    var stats: StatsModel? = null

    @SerializedName("similar")
    @Expose
    var similar: Similar? = null

    @SerializedName("tags")
    @Expose
    var tags: TagsModel? = null

    @SerializedName("bio")
    @Expose
    var bio: BioModel? = null
    override fun toString(): String {
        return "LastFmArtistData{" +
                "name='" + name + '\'' +
                ", mbid='" + mbid + '\'' +
                ", url='" + url + '\'' +
                ", image=" + image +
                ", streamable='" + streamable + '\'' +
                ", ontour='" + ontour + '\'' +
                ", stats=" + stats +
                ", similar=" + similar +
                ", tags=" + tags +
                ", bio=" + bio +
                '}'
    }
}