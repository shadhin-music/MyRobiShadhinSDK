package com.myrobi.shadhinmusiclibrary.data.model.profile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserProfileResponseModel(
    @SerializedName("Data")
    var `data`: Data? = null,
    @SerializedName("Message")
    var message: String? = null,
    @SerializedName("Status")
    var status: String? = null
)