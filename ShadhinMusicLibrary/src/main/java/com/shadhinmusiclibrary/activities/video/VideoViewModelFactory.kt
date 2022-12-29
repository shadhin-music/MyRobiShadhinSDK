package com.shadhinmusiclibrary.activities.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.download.room.DatabaseClient

class VideoViewModelFactory(private val databaseClient: DatabaseClient) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

//        return VideoViewModel(databaseClient) as T
        return VideoViewModel() as T
    }
}