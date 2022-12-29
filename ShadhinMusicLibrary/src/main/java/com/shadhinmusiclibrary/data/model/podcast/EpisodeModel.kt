package com.shadhinmusiclibrary.data.model.podcast

import androidx.annotation.Keep

@Keep
internal data class EpisodeModel(
    var Id: Int,
    val ShowId: String,
    val Code: String,
    val Name: String,
    val ImageUrl: String,
    val Details: String,
    val ContentType: String,
    val fav: String,
    val Sort: Int,
    val IsPaid: Boolean,
    val IsCommentPaid: Boolean,
    val TrackList: MutableList<SongTrackModel>
)