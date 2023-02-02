package com.myrobi.shadhinmusiclibrary.fragments.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentResponse
import com.myrobi.shadhinmusiclibrary.data.model.podcast.PodcastModel
import com.myrobi.shadhinmusiclibrary.data.repository.PodcastRepository
import com.myrobi.shadhinmusiclibrary.data.repository.comments.CommentsRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch

internal class CommentsViewModel(private val commentsRepository: CommentsRepository) : ViewModel() {
    private val _getComments: MutableLiveData<ApiResponse<CommentResponse>> = MutableLiveData()
    val getComments: LiveData<ApiResponse<CommentResponse>> = _getComments

    fun getAllComments(code: String, type: String, page: Int) =
        viewModelScope.launch {
            val response =
                commentsRepository.getAllComments(code,type,page)
            _getComments.postValue(response)
        }

//    fun fetchPodcastShowContent(podType: String, contentType: String, isPaid: Boolean) =
//        viewModelScope.launch {
//            val response =
//                podcastRepository.fetchPodcastShow(podType, contentType, isPaid)
//            _podcastContent.postValue(response)
//        }
}