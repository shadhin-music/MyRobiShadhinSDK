package com.shadhinmusiclibrary.fragments.artist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.ArtistAlbumModel

import com.shadhinmusiclibrary.data.repository.ArtistAlbumContentRepository
import com.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch


internal class ArtistAlbumsViewModel (private val artistAlbumRepository: ArtistAlbumContentRepository): ViewModel() {
    private val _artistAlbumContent: MutableLiveData<ApiResponse<ArtistAlbumModel>> = MutableLiveData()
    val artistAlbumContent: LiveData<ApiResponse<ArtistAlbumModel>> = _artistAlbumContent

    fun fetchArtistAlbum(type: String,id:String) = viewModelScope.launch {
        if(id.isNotEmpty()) {
            val response = artistAlbumRepository.fetchArtistAlbum(type, id)
            _artistAlbumContent.postValue(response)
        }
    }
}