package com.myrobi.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class ArtistModel(
    val data: MutableList<SearchDataModel>,
    val message: String,
    val status: String,
    val type: String
)