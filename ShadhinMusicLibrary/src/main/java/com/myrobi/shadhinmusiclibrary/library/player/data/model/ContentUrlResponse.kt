package com.myrobi.shadhinmusiclibrary.library.player.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class ContentUrlResponse(
    @SerializedName("Data")
    var `data`: String? = null,
    @SerializedName("Message")
    var message: String? = null
)