package com.myrobi.shadhinmusiclibrary.data.repository.comments

import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.data.remote.CommentApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class CommentsRepository(private val commentApiService: ApiService) {
    suspend fun getAllComments(code:String,type:String,page:Int) = safeApiCall {
        commentApiService.getAllComment(code,type,page)
    }
}