package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class FeaturedPodcastRepository(private val apiService: ApiService) {
    suspend fun fetchFeaturedPodcast(isPaid: Boolean) = safeApiCall {
        apiService.fetchFeturedPodcast(isPaid)
    }
    suspend fun fetchPodcastSeeAll(isPaid: Boolean) = safeApiCall {
        apiService.fetchPodcastSeeAll(isPaid)
    }
    suspend fun fetchShadhinPodcastSeeAll(isPaid: Boolean) = safeApiCall {
        apiService.fetchShadhinPodcastSeeAll(isPaid)
    }
}