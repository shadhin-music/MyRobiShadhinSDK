package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class HomeContentRepository(private val apiService: ApiService) {
    suspend fun fetchHomeData(pageNumber: Int?, isPaid: Boolean?) = safeApiCall {
        apiService.fetchHomeData(pageNumber, isPaid)
    }
    suspend fun fetchPatchData(patchCode: String) = safeApiCall {
        apiService.fetchPatchData(patchCode)
    }

    suspend fun rbtURL() = safeApiCall {
        apiService.rbtURL()
    }
}