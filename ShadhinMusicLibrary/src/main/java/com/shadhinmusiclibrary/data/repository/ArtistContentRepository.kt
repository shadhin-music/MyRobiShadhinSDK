package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class ArtistContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistBiogrphyData(artist: String) = safeApiCall {
        apiService.fetchArtistBiography(artist)
    }
}