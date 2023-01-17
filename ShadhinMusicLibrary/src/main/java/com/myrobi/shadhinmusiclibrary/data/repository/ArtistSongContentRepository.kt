package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class ArtistSongContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistSongData(artist: String) = safeApiCall {
        apiService.fetchArtistSongs(artist)
    }
}