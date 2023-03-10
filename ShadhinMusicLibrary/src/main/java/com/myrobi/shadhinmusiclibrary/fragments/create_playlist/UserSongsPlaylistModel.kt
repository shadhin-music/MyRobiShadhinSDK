package com.myrobi.shadhinmusiclibrary.fragments.create_playlist


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserSongsPlaylistModel(
    @SerializedName("data")
    var `data`: MutableList<UserSongsPlaylistDataModel>?,
    @SerializedName("fav")
    var fav: Any?,
    @SerializedName("follow")
    var follow: Any?,
    @SerializedName("image")
    var image: Any?,
    @SerializedName("isPaid")
    var isPaid: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("type")
    var type: String?
)