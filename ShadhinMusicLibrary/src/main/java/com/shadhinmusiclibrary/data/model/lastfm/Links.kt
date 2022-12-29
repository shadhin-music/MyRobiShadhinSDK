package com.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class Links {
    @SerializedName("link")
    @Expose
    var link: LinkModel? = null
}