package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class FeaturedPodcastModel(
    val `data`: List<FeaturedPodcastDataModel>,
    val message: String,
    val status: String
)