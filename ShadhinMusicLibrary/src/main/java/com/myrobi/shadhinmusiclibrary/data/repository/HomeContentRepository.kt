package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


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