package com.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class TagModel {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null
}