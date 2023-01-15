package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class AmartunesContentRepository(private val apiService: ApiService) {
    suspend fun rbtURL() = safeApiCall {
        apiService.rbtURL()
    }
}
