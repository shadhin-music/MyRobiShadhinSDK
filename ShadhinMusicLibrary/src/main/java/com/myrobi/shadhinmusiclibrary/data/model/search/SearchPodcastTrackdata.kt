package com.myrobi.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class SearchPodcastTrackdata(
    val AlbumId: String,
    val AlbumImage: Any,
    val AlbumName: Any,
    val Artist: String,
    val ArtistId: Any,
    val ArtistImage: Any,
    val Banner: Any,
    val ClientValue: Int,
    val ContentID: String,
    val ContentType: String,
    val CreateDate: Any,
    val Duration: String,
    val Follower: Any,
    val IsPaid: Boolean,
    val NewBanner: Any,
    val PlayCount: Int,
    val PlayListId: Any,
    val PlayListImage: Any,
    val PlayListName: Any,
    val PlayUrl: String,
    val RootId: Any,
    val RootType: Any,
    val Seekable: Boolean,
    val TeaserUrl: Any,
    val TrackType: String,
    val Type: String,
    val fav: Any,
    val image: String,
    val imageWeb: Any,
    val title: String
)