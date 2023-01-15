package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.PopularArtistRepository

internal class PopularArtistViewModelFactory(private val popularArtistRepository: PopularArtistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PopularArtistViewModel(popularArtistRepository) as T
    }
}