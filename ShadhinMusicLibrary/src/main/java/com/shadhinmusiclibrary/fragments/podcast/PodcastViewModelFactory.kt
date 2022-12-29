package com.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.PodcastRepository

internal class PodcastViewModelFactory(private val podcastRepository: PodcastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PodcastViewModel(podcastRepository) as T
    }
}