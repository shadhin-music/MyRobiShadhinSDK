package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class HomePatchItemModel(
    val Code: String,
    var ContentType: String,
    var Data: List<HomePatchDetailModel>,
    var Design: String,
    val Name: String,
    val Sort: Int,
    val Total: Int
) : Serializable