package com.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.FeaturedPodcastRepository

internal class FeaturedPodcastViewModelFactory(private val featuredPodcastRepository: FeaturedPodcastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeaturedPodcastViewModel(featuredPodcastRepository) as T
    }
}