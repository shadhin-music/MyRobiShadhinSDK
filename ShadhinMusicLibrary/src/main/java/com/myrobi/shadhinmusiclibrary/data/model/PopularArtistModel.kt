package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class PopularArtistModel(
    val Total: Int,
    val `data`: List<PodcastDetailsModel>,
    val message: String,
    val status: String
)