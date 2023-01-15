package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class FeaturedTracklistRepository(private val apiService: ApiService) {
    suspend fun fetchFeaturedTrackList() = safeApiCall {
        apiService.fetchFeaturedTrackList()
    }
}