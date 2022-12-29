package com.shadhinmusiclibrary.fragments.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.CreatePlaylistRepository
import com.shadhinmusiclibrary.data.repository.FavContentRepository
import com.shadhinmusiclibrary.fragments.create_playlist.CreateplaylistViewModel

internal class FavViewModelFactory (private val favContentRepository: FavContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(favContentRepository) as T
    }
}