package com.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class Similar {
    @SerializedName("artist")
    @Expose
    var artist: List<LastFmArtistModel>? = null
}