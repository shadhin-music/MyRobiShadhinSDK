package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class PopularArtistRepository(private val apiService: ApiService) {
    suspend fun fetchPopularArtist() = safeApiCall {
        apiService.fetchPopularArtist()
    }

    suspend fun fetchLatestVideo() = safeApiCall {
        apiService.fetchLatestVideo()
    }
}