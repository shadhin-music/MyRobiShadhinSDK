package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class SearchRepository(private val apiService: ApiService) {
    suspend fun getSearch(keyword: String) = safeApiCall {
        apiService.getSearch(keyword)
    }

    suspend fun getTopTrendingItems(type: String) = safeApiCall {
        apiService.getTopTrendingItems(type)
    }
}