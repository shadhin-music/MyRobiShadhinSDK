package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class ArtistContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistBiogrphyData(artist: String) = safeApiCall {
        apiService.fetchArtistBiography(artist)
    }
}