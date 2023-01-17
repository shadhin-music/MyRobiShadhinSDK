package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.model.HistoryModel
import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class ClientActivityContentRepository(private val apiService: ApiService) {

    suspend fun fetchPatchClickHistory(historyData: HistoryModel) = safeApiCall {
        apiService.fetchPatchClickHistory(historyData)
    }
}