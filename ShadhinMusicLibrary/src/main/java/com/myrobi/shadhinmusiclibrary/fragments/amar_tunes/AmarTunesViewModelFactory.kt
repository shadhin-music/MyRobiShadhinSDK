package com.myrobi.shadhinmusiclibrary.fragments.amar_tunes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.AmartunesContentRepository


internal class AmarTunesViewModelFactory(private val amartunesContentRepository: AmartunesContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AmarTunesViewModel(amartunesContentRepository) as T
    }
}
