package com.myrobi.shadhinmusiclibrary.fragments.artist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.APIResponse
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedSongDetailModel
import com.myrobi.shadhinmusiclibrary.data.repository.FeaturedTracklistRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch


internal class FeaturedTracklistViewModel(private val featuredTracklistRepository: FeaturedTracklistRepository) :
    ViewModel() {

    private val _featuredTracklistContent: MutableLiveData<ApiResponse<APIResponse<MutableList<FeaturedSongDetailModel>>>> =
        MutableLiveData()
    val featuredTracklistContent: LiveData<ApiResponse<APIResponse<MutableList<FeaturedSongDetailModel>>>> =
        _featuredTracklistContent

    fun fetchFeaturedTrackList() = viewModelScope.launch {
        val response = featuredTracklistRepository.fetchFeaturedTrackList()
        _featuredTracklistContent.postValue(response)
    }
}