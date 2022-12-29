package com.shadhinmusiclibrary.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserSessionResponse(
    @SerializedName("Data")
    var `data`: Any? = null,
    @SerializedName("Message")
    var message: Any? = null,
    @SerializedName("Status")
    var status: String? = null
)