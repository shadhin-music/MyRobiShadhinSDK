package com.myrobi.shadhinmusiclibrary.fragments.create_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistBannerContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.CreatePlaylistRepository


internal class CreatePlaylistViewModelFactory(private val createPlaylistRepository: CreatePlaylistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateplaylistViewModel(createPlaylistRepository) as T
    }
}