package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
data class FeaturedPodcastDataModel(
    val Data: List<FeaturedPodcastDetailsModel>,
    val Design: Any,
    val PatchName: String,
    var PatchType: String
)