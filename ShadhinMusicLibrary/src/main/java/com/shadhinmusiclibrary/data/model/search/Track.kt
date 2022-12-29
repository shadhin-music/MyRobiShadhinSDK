package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class Track(
    val data: MutableList<SearchDataModel>,
    val message: String,
    val status: String,
    val type: String
)