package com.myrobi.shadhinmusiclibrary.data.remote

import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentReplyResponse
import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface CommentApiService {
    @GET("Comment/GetListV2/{id}/{type}/{page}")
    fun getAllComment(
        @Path("id") id: String?,
        @Path("type") type: String?,
        @Path("page") page: Int,
    ): CommentResponse

    @GET("Reply/GetList/{id}")
    fun getAllReply(@Path("id") id: Int): CommentReplyResponse

//    @POST("Comment/CreateV2")
//    fun addComment(@Body body: HashMap<String?, Any?>?): CommentCreateReponse
}