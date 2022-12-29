package com.shadhinmusiclibrary.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.SearchRepository


internal class SearchViewModelFactory(private val searchRepository: SearchRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return SearchViewModel(searchRepository) as T
    }
}