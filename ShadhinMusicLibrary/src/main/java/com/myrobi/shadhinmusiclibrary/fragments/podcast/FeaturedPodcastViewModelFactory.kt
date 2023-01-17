package com.myrobi.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.FeaturedPodcastRepository

internal class FeaturedPodcastViewModelFactory(private val featuredPodcastRepository: FeaturedPodcastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeaturedPodcastViewModel(featuredPodcastRepository) as T
    }
}