package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class PodcastRepository(private val apiService: ApiService) {

    suspend fun fetchPodcastByID(
        podType: String,
        episodeId: String,
        contentType: String,
        isPaid: Boolean
    ) = safeApiCall {
        apiService.fetchPodcastByID(podType, episodeId, contentType, isPaid)
    }

    suspend fun fetchPodcastShow(
        podType: String,
        contentType: String,
        isPaid: Boolean
    ) = safeApiCall {
        apiService.fetchPodcastShow(podType, contentType, isPaid)
    }
}