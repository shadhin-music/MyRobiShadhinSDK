package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class SearchRepository(private val apiService: ApiService) {
    suspend fun getSearch(keyword: String) = safeApiCall {
        apiService.getSearch(keyword)
    }

    suspend fun getTopTrendingItems(type: String) = safeApiCall {
        apiService.getTopTrendingItems(type)
    }
}