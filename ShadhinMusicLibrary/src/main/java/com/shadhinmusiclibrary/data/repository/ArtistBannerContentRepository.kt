package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class ArtistBannerContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistBannerData(id: String) = safeApiCall {
        apiService.fetchArtistBannerData(id)
    }
}