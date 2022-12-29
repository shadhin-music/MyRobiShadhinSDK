package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class HistoryModel {
    @SerializedName("patchCode")
    @Expose
    var patchCode: String? = null
}