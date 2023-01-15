package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistAlbumContentRepository


internal class ArtistAlbumViewModelFactory(private val artistAlbumContentRepository: ArtistAlbumContentRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return ArtistAlbumsViewModel(artistAlbumContentRepository) as T
    }
}