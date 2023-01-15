package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class FeaturedLatestTrackListModel(
    val `data`: MutableList<FeaturedLatestTrackListDataModel>,
    val message: String,
    val status: String
)