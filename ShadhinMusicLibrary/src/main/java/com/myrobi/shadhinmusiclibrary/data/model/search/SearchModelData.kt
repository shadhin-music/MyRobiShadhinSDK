package com.myrobi.shadhinmusiclibrary.data.model.search

import androidx.annotation.Keep

@Keep
internal data class SearchModelData(
    val Album: CommonSearchData,
    val Artist: CommonSearchData,
    val PodcastEpisode: CommonSearchData,
    val PodcastShow: CommonSearchData,
    val PodcastTrack: CommonSearchData,
    val Track: CommonSearchData,
    val Video: CommonSearchData
)