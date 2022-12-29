package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.model.HistoryModel
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class ClientActivityContentRepository(private val apiService: ApiService) {

    suspend fun fetchPatchClickHistory(historyData: HistoryModel) = safeApiCall {
        apiService.fetchPatchClickHistory(historyData)
    }
}