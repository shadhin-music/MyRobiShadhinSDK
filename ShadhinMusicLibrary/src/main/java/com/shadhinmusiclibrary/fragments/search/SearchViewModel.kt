package com.shadhinmusiclibrary.fragments.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.search.SearchModel
import com.shadhinmusiclibrary.data.model.search.TopTrendingModel
import com.shadhinmusiclibrary.data.repository.SearchRepository
import com.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch


internal class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _searchArtistContent: MutableLiveData<ApiResponse<SearchModel>> = MutableLiveData()
    val searchArtistContent: LiveData<ApiResponse<SearchModel>> = _searchArtistContent

    private val _searchAlbumContent: MutableLiveData<ApiResponse<SearchModel>> = MutableLiveData()
    val searchAlbumContent: LiveData<ApiResponse<SearchModel>> = _searchAlbumContent

    private val _searchTracksContent: MutableLiveData<ApiResponse<SearchModel>> = MutableLiveData()
    val searchTracksContent: LiveData<ApiResponse<SearchModel>> = _searchTracksContent

    private val _searchVideoContent: MutableLiveData<ApiResponse<SearchModel>> = MutableLiveData()
    val searchVideoContent: LiveData<ApiResponse<SearchModel>> = _searchVideoContent

    private val _searchPodcastShowContent: MutableLiveData<ApiResponse<SearchModel>> =
        MutableLiveData()
    val searchPodcastShowContent: LiveData<ApiResponse<SearchModel>> = _searchPodcastShowContent

    private val _searchPodcastEpisodeContent: MutableLiveData<ApiResponse<SearchModel>> =
        MutableLiveData()
    val searchPodcastEpisodeContent: LiveData<ApiResponse<SearchModel>> =
        _searchPodcastEpisodeContent

    private val _searchPodcastTrackContent: MutableLiveData<ApiResponse<SearchModel>> =
        MutableLiveData()
    val searchPodcastTrackContent: LiveData<ApiResponse<SearchModel>> = _searchPodcastTrackContent

    private val _topTrendingContent: MutableLiveData<ApiResponse<TopTrendingModel>> =
        MutableLiveData()
    val topTrendingContent: LiveData<ApiResponse<TopTrendingModel>> = _topTrendingContent

    private val _topTrendingVideoContent: MutableLiveData<ApiResponse<TopTrendingModel>> =
        MutableLiveData()
    val topTrendingVideoContent: LiveData<ApiResponse<TopTrendingModel>> = _topTrendingVideoContent

    fun getSearchArtist(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchArtistContent.postValue(response)
    }

    fun getSearchAlbum(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchAlbumContent.postValue(response)
    }

    fun getSearchTracks(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchTracksContent.postValue(response)
    }

    fun getSearchVideo(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchVideoContent.postValue(response)
    }

    fun getSearchPodcastShow(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchPodcastShowContent.postValue(response)
    }

    fun getSearchPodcastEpisode(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchPodcastEpisodeContent.postValue(response)
    }

    fun getSearchPodcastTrack(keyword: String) = viewModelScope.launch {
        val response = searchRepository.getSearch(keyword)
        _searchPodcastTrackContent.postValue(response)
    }

    fun getTopTrendingItems(type: String) = viewModelScope.launch {
        val response = searchRepository.getTopTrendingItems(type)
        _topTrendingContent.postValue(response)
    }

    fun getTopTrendingVideos(type: String) = viewModelScope.launch {
        val response = searchRepository.getTopTrendingItems(type)
        _topTrendingVideoContent.postValue(response)
    }
}