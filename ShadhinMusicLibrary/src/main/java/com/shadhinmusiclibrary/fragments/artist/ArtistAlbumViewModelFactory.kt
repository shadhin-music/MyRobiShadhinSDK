package com.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.ArtistAlbumContentRepository


internal class ArtistAlbumViewModelFactory(private val artistAlbumContentRepository: ArtistAlbumContentRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return ArtistAlbumsViewModel(artistAlbumContentRepository) as T
    }
}