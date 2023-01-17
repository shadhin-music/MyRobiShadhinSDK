package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class ArtistAlbumContentRepository(private val apiService: ApiService) {
    suspend fun fetchArtistAlbum(type: String, id: String) = safeApiCall {
        apiService.fetchArtistAlbum(type, id)
    }

}