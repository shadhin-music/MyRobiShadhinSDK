package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.FeaturedTracklistRepository

internal class FeaturedTracklistViewModelFactory(private val featuredTracklistRepository: FeaturedTracklistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeaturedTracklistViewModel(featuredTracklistRepository) as T
    }
}