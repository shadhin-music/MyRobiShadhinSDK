package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData

internal interface HomeCallBack {
    fun onClickItemAndAllItem(itemPosition: Int, selectedHomePatchItem: HomePatchItemModel)
    fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel)
    fun onArtistAlbumClick(itemPosition: Int, artistAlbumModelData: List<ArtistAlbumModelData>) {}
    fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>)

}