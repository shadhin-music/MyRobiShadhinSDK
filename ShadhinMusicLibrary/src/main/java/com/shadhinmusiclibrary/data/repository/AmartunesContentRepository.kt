package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class AmartunesContentRepository(private val apiService: ApiService) {
    suspend fun rbtURL() = safeApiCall {
        apiService.rbtURL()
    }
}
