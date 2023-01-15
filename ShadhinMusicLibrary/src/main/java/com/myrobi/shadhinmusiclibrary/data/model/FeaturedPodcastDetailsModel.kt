package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
data class FeaturedPodcastDetailsModel(
    val About: String,
    val CreateDate: String,
    val ContentType: String,
    val Duration: String,
    val EndDate: String,
    val EpisodeCode: String,
    val EpisodeId: String,
    val EpisodeName: String,
    val ImageUrl: String,
    val IsComingSoon: Boolean,
    val IsPaid: Boolean,
    val PatchType: String,
    val PlayUrl: String,
    val Presenter: String,
    val Seekable: Boolean,
    val ShowCode: String,
    val ShowId: String,
    val ShowName: String,
    val Sort: Int,
    val StartDate: String,
    val TotalPlayCount: Int,
    val TrackName: String,
    val TrackType: String,
    val TracktId: String,
    val fav: String
)