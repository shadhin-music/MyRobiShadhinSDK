package com.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class PodcastShow(
    val data: List<SearchDataModel>,
    val message: String,
    val status: String,
    val type: String
)