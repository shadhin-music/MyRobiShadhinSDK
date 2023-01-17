package com.myrobi.shadhinmusiclibrary.data.model.podcast

import androidx.annotation.Keep

@Keep
internal data class PodcastModel(
    val message: String,
    val status: String,
    val `data`: DataModel
)