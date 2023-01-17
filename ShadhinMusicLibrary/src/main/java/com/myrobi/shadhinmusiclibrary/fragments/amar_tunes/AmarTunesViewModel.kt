package com.myrobi.shadhinmusiclibrary.fragments.amar_tunes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.RBTModel
import com.myrobi.shadhinmusiclibrary.data.repository.AmartunesContentRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse

import kotlinx.coroutines.launch

internal class AmarTunesViewModel(private val amartunesContentRepository: AmartunesContentRepository) :
    ViewModel() {
    private val _urlContent: MutableLiveData<ApiResponse<RBTModel>> = MutableLiveData()
    val urlContent: LiveData<ApiResponse<RBTModel>> = _urlContent

    fun fetchRBTURL() = viewModelScope.launch {
        val response = amartunesContentRepository.rbtURL()
        _urlContent.postValue(response)
    }
}