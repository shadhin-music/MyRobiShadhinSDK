package com.myrobi.shadhinmusiclibrary.fragments.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.CreatePlaylistRepository
import com.myrobi.shadhinmusiclibrary.data.repository.FavContentRepository
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.CreateplaylistViewModel

internal class FavViewModelFactory (private val favContentRepository: FavContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(favContentRepository) as T
    }
}