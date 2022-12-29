package com.shadhinmusiclibrary.data.model.podcast

import androidx.annotation.Keep

@Keep
internal data class DataModel(
    val Id: Int,
    val Code: String,
    val Name: String,
    val ImageUrl: String,
    val ProductBy: String,
    val Presenter: String,
    val About: String,
    val Duration: String,
    val ContentType: String,
    val Sort: Int,
    val IsComingSoon: Boolean,
    val EpisodeList: MutableList<EpisodeModel>
)