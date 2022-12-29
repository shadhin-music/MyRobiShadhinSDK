package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class FeaturedTracklistRepository(private val apiService: ApiService) {
    suspend fun fetchFeaturedTrackList() = safeApiCall {
        apiService.fetchFeaturedTrackList()
    }
}