package com.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class LinkModel {
    @SerializedName("#text")
    @Expose
    var text: String? = null

    @SerializedName("rel")
    @Expose
    var rel: String? = null

    @SerializedName("href")
    @Expose
    var href: String? = null
}