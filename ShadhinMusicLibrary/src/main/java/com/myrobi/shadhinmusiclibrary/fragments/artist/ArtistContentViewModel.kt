package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.ArtistContentModel
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistSongContentRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse

import kotlinx.coroutines.launch

internal class ArtistContentViewModel(private val artistContentRepository: ArtistSongContentRepository) :
    ViewModel() {

    private val _artistSongContent: MutableLiveData<ApiResponse<ArtistContentModel>> =
        MutableLiveData()
    val artistSongContent: LiveData<ApiResponse<ArtistContentModel>> = _artistSongContent
    fun fetchArtistSongData(artist: String) = viewModelScope.launch {
        val response = artistContentRepository.fetchArtistSongData(artist)
        _artistSongContent.postValue(response)
    }
}