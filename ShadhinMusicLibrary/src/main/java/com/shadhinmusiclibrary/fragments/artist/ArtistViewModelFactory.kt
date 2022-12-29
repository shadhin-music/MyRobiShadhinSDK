package com.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.ArtistContentRepository

internal class ArtistViewModelFactory(private val artistContentRepository: ArtistContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArtistViewModel(artistContentRepository) as T
    }
}