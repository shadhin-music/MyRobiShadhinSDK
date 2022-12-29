package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class LatestVideoModel(
    val `data`: List<LatestVideoModelDataModel>,
    val message: String,
    val status: String
)