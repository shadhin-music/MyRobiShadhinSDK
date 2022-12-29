package com.shadhinmusiclibrary.fragments.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.AlbumContentRepository

internal class AlbumViewModelFactory(private val albumRepository: AlbumContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(albumRepository) as T
    }
}