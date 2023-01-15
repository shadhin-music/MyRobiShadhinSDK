package com.myrobi.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.podcast.PodcastModel
import com.myrobi.shadhinmusiclibrary.data.repository.PodcastRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch

internal class PodcastViewModel(private val podcastRepository: PodcastRepository) : ViewModel() {
    private val _podcastContent: MutableLiveData<ApiResponse<PodcastModel>> = MutableLiveData()
    val podcastDetailsContent: LiveData<ApiResponse<PodcastModel>> = _podcastContent

    fun fetchPodcastContent(podType: String, episodeId: String, contentType: String, isPaid: Boolean) =
        viewModelScope.launch {
            val response =
                podcastRepository.fetchPodcastByID(podType, episodeId, contentType, isPaid)
            _podcastContent.postValue(response)
        }

    fun fetchPodcastShowContent(podType: String, contentType: String, isPaid: Boolean) =
        viewModelScope.launch {
            val response =
                podcastRepository.fetchPodcastShow(podType, contentType, isPaid)
            _podcastContent.postValue(response)
        }
}