package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class TopTrendingModel(
    val data: MutableList<TopTrendingDataModel>,
    val fav: Any,
    val follow: Any,
    val image: Any,
    val isPaid: Boolean,
    val message: String,
    val status: String,
    val type: String
)