package com.myrobi.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class LastFmImageModel {
    @SerializedName("#text")
    @Expose
    var text: String? = null

    @SerializedName("size")
    @Expose
    var size: String? = null
}