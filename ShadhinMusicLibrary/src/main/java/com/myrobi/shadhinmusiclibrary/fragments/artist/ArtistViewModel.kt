package com.myrobi.shadhinmusiclibrary.fragments.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.lastfm.LastFmResult
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistContentRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse

import kotlinx.coroutines.launch

internal class ArtistViewModel(private val artistContentRepository: ArtistContentRepository) :
    ViewModel() {
    private val _artistBioContent: MutableLiveData<ApiResponse<LastFmResult>> = MutableLiveData()
    val artistBioContent: LiveData<ApiResponse<LastFmResult>> = _artistBioContent
    fun fetchArtistBioData(artist: String) = viewModelScope.launch {
        val response = artistContentRepository.fetchArtistBiogrphyData(artist)
        _artistBioContent.postValue(response)
    }
}