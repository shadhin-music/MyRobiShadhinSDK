package com.myrobi.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class StatsModel {
    @SerializedName("listeners")
    @Expose
    var listeners: String? = null

    @SerializedName("playcount")
    @Expose
    var playcount: String? = null
}