package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal class PatchDataModel {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: HomePatchItemModel?=null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("fav")
    @Expose
    var fav: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("follow")
    @Expose
    var follow: String? = null

    @SerializedName("MonthlyListener")
    @Expose
    var monthlyListener: String? = null
}