package com.myrobi.shadhinmusiclibrary.data.model.lastfm

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Keep
internal class LastFmImageDataModel {
    @SerializedName("#text")
    @Expose
    var text: String? = null

    @SerializedName("size")
    @Expose
    var size: String? = null

    override fun toString(): String {
        return "LastFmImageData{" +
                "text='" + text + '\'' +
                ", size='" + size + '\'' +
                '}'
    }
}