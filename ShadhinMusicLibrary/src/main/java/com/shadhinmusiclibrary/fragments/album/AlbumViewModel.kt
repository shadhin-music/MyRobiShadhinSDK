package com.shadhinmusiclibrary.fragments.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.APIResponse
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.repository.AlbumContentRepository
import com.shadhinmusiclibrary.utils.ApiResponse
import com.shadhinmusiclibrary.utils.AppConstantUtils
import kotlinx.coroutines.launch

internal class AlbumViewModel(private val albumContentRepository: AlbumContentRepository) :
    ViewModel() {

    private val _albumContent: MutableLiveData<ApiResponse<APIResponse<MutableList<SongDetailModel>>>> =
        MutableLiveData()

    private val _allRadios: MutableLiveData<ApiResponse<APIResponse<MutableList<HomePatchDetailModel>>>> =
        MutableLiveData()

    val albumContent: LiveData<ApiResponse<APIResponse<MutableList<SongDetailModel>>>> =
        _albumContent

    val radiosContent: LiveData<ApiResponse<APIResponse<MutableList<HomePatchDetailModel>>>> =
        _allRadios

    fun fetchAlbumContent(contentId: String) = viewModelScope.launch {
        val response = albumContentRepository.fetchAlbumContent(contentId)
        _albumContent.postValue(response)
    }

    fun fetchSingleContent(contentId: String) = viewModelScope.launch {
        val response = albumContentRepository.fetchSingleContent(contentId)
        _albumContent.postValue(response)
    }

    fun fetchPlaylistContent(contentId: String) = viewModelScope.launch {
        val response = albumContentRepository.fetchPlaylistContent(contentId)
        _albumContent.postValue(response)
    }

    fun fetchGetAllRadio() = viewModelScope.launch {
        val response = albumContentRepository.fetchGetAllRadio()
        _allRadios.postValue(response)
    }
}