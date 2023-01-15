package com.myrobi.shadhinmusiclibrary.callBackService

import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel

internal interface FeaturedPodcastOnItemClickCallback {
    fun onRootClickItem(episode: MutableList<FeaturedPodcastDetailsModel>, clickItemPosition: Int)
    fun onClickItem(episode: MutableList<FeaturedPodcastDetailsModel>, clickItemPosition: Int)
    fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        episode: MutableList<FeaturedPodcastDetailsModel>
    )
}