package com.shadhinmusiclibrary.fragments.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.data.repository.ClientActivityContentRepository

internal class ClientActivityViewModelFactory(private val clientActivityRepos: ClientActivityContentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientActivityViewModel(clientActivityRepos) as T
    }
}