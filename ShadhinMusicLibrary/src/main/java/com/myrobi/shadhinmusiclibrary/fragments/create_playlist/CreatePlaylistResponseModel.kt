package com.myrobi.shadhinmusiclibrary.fragments.create_playlist


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreatePlaylistResponseModel(
    @SerializedName("Message")
    var message: Any?,
    @SerializedName("Status")
    var status: String?
)