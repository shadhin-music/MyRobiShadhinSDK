package com.myrobi.shadhinmusiclibrary.data.model.comments


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class CommentReplyResponse(
    @SerializedName("Data")
    var `data`: List<CommentReplyData>? = null,
    @SerializedName("Message")
    var message: String? = null,
    @SerializedName("Status")
    var status: Boolean? = null,
    @SerializedName("TotalData")
    var totalData: Int? = null,
    @SerializedName("TotalPage")
    var totalPage: Int? = null
)