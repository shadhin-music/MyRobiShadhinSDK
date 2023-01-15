package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class ArtistBannerContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistBannerData(id: String) = safeApiCall {
        apiService.fetchArtistBannerData(id)
    }
}