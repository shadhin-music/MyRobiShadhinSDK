package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData

internal interface HomeCallBack {
    fun onClickItemAndAllItem(itemPosition: Int, selectedHomePatchItem: HomePatchItemModel)
    fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel)
    fun onArtistAlbumClick(itemPosition: Int, artistAlbumModelData: List<ArtistAlbumModelData>) {}
    fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>)

}