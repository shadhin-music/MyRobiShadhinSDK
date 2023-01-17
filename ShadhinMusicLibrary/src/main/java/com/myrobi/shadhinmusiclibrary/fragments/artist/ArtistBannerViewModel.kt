package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.ArtistBannerModel
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistBannerContentRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse

import kotlinx.coroutines.launch

internal class ArtistBannerViewModel(private val artistBannerContentRepository: ArtistBannerContentRepository) :
    ViewModel() {
    private val _artistBannerContent: MutableLiveData<ApiResponse<ArtistBannerModel>> =
        MutableLiveData()
    val artistBannerContent: LiveData<ApiResponse<ArtistBannerModel>> = _artistBannerContent

    fun fetchArtistBannerData(id: String) = viewModelScope.launch {
        val response = artistBannerContentRepository.fetchArtistBannerData(id)
        _artistBannerContent.postValue(response)
    }
}