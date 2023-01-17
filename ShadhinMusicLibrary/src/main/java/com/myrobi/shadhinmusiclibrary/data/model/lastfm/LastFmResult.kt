package com.myrobi.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class LastFmResult {
    @SerializedName("artist")
    @Expose
    var artist: LastFmArtistDataModel? = null
}