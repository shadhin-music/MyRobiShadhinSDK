package com.shadhinmusiclibrary.fragments.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.HistoryModel
import com.shadhinmusiclibrary.data.repository.ClientActivityContentRepository
import kotlinx.coroutines.launch

internal class ClientActivityViewModel(private val clientActivityVm: ClientActivityContentRepository) :
    ViewModel() {

    fun fetchPatchClickHistory(historyData: HistoryModel) = viewModelScope.launch {
        val data = clientActivityVm.fetchPatchClickHistory(historyData)
        Log.e("CAVM", "fetchPatchClickHistory: " + data.data?.status)
    }
}