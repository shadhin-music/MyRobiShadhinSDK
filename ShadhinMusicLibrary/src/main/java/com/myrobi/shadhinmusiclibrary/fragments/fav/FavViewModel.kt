package com.myrobi.shadhinmusiclibrary.fragments.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataResponseModel
import com.myrobi.shadhinmusiclibrary.data.repository.FavContentRepository
import kotlinx.coroutines.launch

internal class FavViewModel(private val favContentRepository: FavContentRepository) : ViewModel() {
    private val _getFavContentAlbum: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val getFavContentAlbum: LiveData<FavDataResponseModel?> = _getFavContentAlbum

    private val _addFavContent: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val addFavContent: LiveData<FavDataResponseModel> = _addFavContent

    private val _deleteFavContent: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val deleteFavContent: LiveData<FavDataResponseModel> = _deleteFavContent

    private val _getFavContentVideo: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val getFavContentVideo: LiveData<FavDataResponseModel> = _getFavContentVideo

    private val _getFavContentSong: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val getFavContentSong: LiveData<FavDataResponseModel> = _getFavContentSong

    private val _getFavContentPlaylist: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val getFavContentPlaylist: LiveData<FavDataResponseModel> = _getFavContentPlaylist
    private val _getFavContentArtist: MutableLiveData<FavDataResponseModel?> = MutableLiveData()
    val getFavContentArtist: LiveData<FavDataResponseModel?> = _getFavContentArtist
    private val _getFavContentPodcast: MutableLiveData<FavDataResponseModel> = MutableLiveData()
    val getFavContentPodcast: LiveData<FavDataResponseModel> = _getFavContentPodcast

    fun getFavContentSong(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("S")
        _getFavContentSong.postValue(response.data)
    }

    fun getFavContentPlaylist(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("P")
        _getFavContentPlaylist.postValue(response.data)
    }

    fun getFavContentArtist(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("A")
        _getFavContentArtist.postValue(response.data)
    }

    fun getFavContentAlbum(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("R")
        _getFavContentAlbum.postValue(response.data)
    }

    fun getFavContentPodcast(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("PD")
        _getFavContentPodcast.postValue(response.data)
    }

    fun getFavContentVideo(type: String) = viewModelScope.launch {
        val response = favContentRepository.fetchAllFavoriteByType("V")
        _getFavContentVideo.postValue(response.data)
    }

    fun addFavContent(contentId: String, contentType: String) = viewModelScope.launch {
        val response = favContentRepository.addFavByType(contentId, contentType)
        _addFavContent.postValue(response.data)
    }

    fun deleteFavContent(contentId: String, contentType: String) = viewModelScope.launch {
        val response = favContentRepository.deleteFavByType(contentId, contentType)
        _deleteFavContent.postValue(response.data)
    }
}