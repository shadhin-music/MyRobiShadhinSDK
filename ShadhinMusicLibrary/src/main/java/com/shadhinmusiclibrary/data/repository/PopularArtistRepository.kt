package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class PopularArtistRepository(private val apiService: ApiService) {
    suspend fun fetchPopularArtist() = safeApiCall {
        apiService.fetchPopularArtist()
    }

    suspend fun fetchLatestVideo() = safeApiCall {
        apiService.fetchLatestVideo()
    }
}