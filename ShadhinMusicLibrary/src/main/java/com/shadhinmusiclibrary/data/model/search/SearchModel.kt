package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class SearchModel(
    val status: String,
    val message: String,
    val data: SearchModelData
)