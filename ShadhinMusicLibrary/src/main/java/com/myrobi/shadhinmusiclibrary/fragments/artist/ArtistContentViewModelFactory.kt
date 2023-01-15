package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistSongContentRepository


internal class ArtistContentViewModelFactory(private val artistSongContentRepository: ArtistSongContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArtistContentViewModel(artistSongContentRepository) as T
    }
}