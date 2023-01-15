package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


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