package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class Album(
    val data: MutableList<SearchDataModel>,
    val message: String,
    val status: String,
    val type: String
)

@Keep
internal data class CommonSearchData(
    val data: MutableList<SearchDataModel>,
    val message: String,
    val status: String,
    val type: String
)