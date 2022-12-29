package com.shadhinmusiclibrary.data.model.fav

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class FavDataResponseModel(
    @SerializedName("data")
    var `data`: MutableList<FavDataModel>?,
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