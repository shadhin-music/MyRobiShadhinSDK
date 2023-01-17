package com.myrobi.shadhinmusiclibrary.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.HomeContentRepository

internal class HomeViewModelFactory(private val homeContentRepository: HomeContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(homeContentRepository) as T
    }
}