package com.myrobi.shadhinmusiclibrary.data.repository.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.myrobi.shadhinmusiclibrary.fragments.podcast.CommentsViewModel

internal class CommentsViewModelFactory (private val commentsRepository: CommentsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(commentsRepository) as T
    }
}