package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class ArtistSongContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistSongData(artist: String) = safeApiCall {
        apiService.fetchArtistSongs(artist)
    }
}