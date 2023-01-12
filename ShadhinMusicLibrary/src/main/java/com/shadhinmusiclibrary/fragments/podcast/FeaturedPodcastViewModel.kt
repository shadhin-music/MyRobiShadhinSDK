package com.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.FeaturedPodcastModel
import com.shadhinmusiclibrary.data.repository.FeaturedPodcastRepository

import com.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch

internal class FeaturedPodcastViewModel(private val featuredPodcastRepository: FeaturedPodcastRepository) :
    ViewModel() {
    private val _featuredpodcastContent: MutableLiveData<ApiResponse<FeaturedPodcastModel>> =
        MutableLiveData()
    val featuredpodcastContent: LiveData<ApiResponse<FeaturedPodcastModel>> =
        _featuredpodcastContent

    private val _podcastSeeAllContent: MutableLiveData<ApiResponse<FeaturedPodcastModel>> =
        MutableLiveData()
    val podcastSeeAllContent: LiveData<ApiResponse<FeaturedPodcastModel>> =
        _podcastSeeAllContent

    fun fetchFeaturedPodcast(isPaid: Boolean) = viewModelScope.launch {
        val response = featuredPodcastRepository.fetchFeaturedPodcast(isPaid)
        _featuredpodcastContent.postValue(response)
    }

    fun fetchPodcastSeeAll(isPaid: Boolean) = viewModelScope.launch {
        val response = featuredPodcastRepository.fetchPodcastSeeAll(isPaid)
        _podcastSeeAllContent.postValue(response)
    }
    fun fetchShadhinPodcastSeeAll(isPaid: Boolean) = viewModelScope.launch {
        val response = featuredPodcastRepository.fetchShadhinPodcastSeeAll(isPaid)
        _podcastSeeAllContent.postValue(response)
    }
}