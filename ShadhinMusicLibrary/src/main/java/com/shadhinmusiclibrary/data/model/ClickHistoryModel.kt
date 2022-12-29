package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


@Keep
internal class ClickHistoryModel {
    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("Message")
    @Expose
    var message: Any? = null

    @SerializedName("Data")
    @Expose
    var data: Any? = null
}