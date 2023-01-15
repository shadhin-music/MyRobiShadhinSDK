package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


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