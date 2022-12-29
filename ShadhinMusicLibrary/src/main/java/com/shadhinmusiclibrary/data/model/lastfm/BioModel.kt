package com.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class BioModel {
    @SerializedName("links")
    @Expose
    var links: Links? = null

    @SerializedName("published")
    @Expose
    var published: String? = null

    @SerializedName("summary")
    @Expose
    var summary: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null
}