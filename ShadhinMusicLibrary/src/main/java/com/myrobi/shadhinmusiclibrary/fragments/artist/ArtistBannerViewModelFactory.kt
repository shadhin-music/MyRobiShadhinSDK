package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistBannerContentRepository


internal class ArtistBannerViewModelFactory(private val artistBannerContentRepository: ArtistBannerContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArtistBannerViewModel(artistBannerContentRepository) as T
    }
}