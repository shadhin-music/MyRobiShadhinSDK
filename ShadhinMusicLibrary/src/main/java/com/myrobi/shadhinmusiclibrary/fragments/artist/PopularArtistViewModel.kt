package com.myrobi.shadhinmusiclibrary.fragments.artist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.LatestVideoModel
import com.myrobi.shadhinmusiclibrary.data.model.PopularArtistModel
import com.myrobi.shadhinmusiclibrary.data.repository.PopularArtistRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch


internal class PopularArtistViewModel(private val popularArtistRepository: PopularArtistRepository) :
    ViewModel() {
    private val _popularArtistContent: MutableLiveData<ApiResponse<PopularArtistModel>> =
        MutableLiveData()
    val popularArtistContent: LiveData<ApiResponse<PopularArtistModel>> = _popularArtistContent
    private val _latestVideoContent: MutableLiveData<ApiResponse<LatestVideoModel>> =
        MutableLiveData()
    val latestVideoContent: LiveData<ApiResponse<LatestVideoModel>> = _latestVideoContent

    fun fetchPouplarArtist() = viewModelScope.launch {
        val response = popularArtistRepository.fetchPopularArtist()
        _popularArtistContent.postValue(response)
    }

    fun fetchLatestVideo() = viewModelScope.launch {
        val response = popularArtistRepository.fetchLatestVideo()
        _latestVideoContent.postValue(response)
    }
}