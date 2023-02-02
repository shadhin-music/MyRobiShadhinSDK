package com.myrobi.shadhinmusiclibrary.data.model.comments


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class CommentData(
    @SerializedName("AdminUserType")
    var adminUserType: String? = null,
    @SerializedName("CommentFavorite")
    var commentFavorite: Boolean? = null,
    @SerializedName("CommentId")
    var commentId: Int? = null,
    @SerializedName("CommentLike")
    var commentLike: Boolean? = null,
    @SerializedName("ContentId")
    var contentId: String? = null,
    @SerializedName("ContentTitle")
    var contentTitle: String? = null,
    @SerializedName("ContentType")
    var contentType: String? = null,
    @SerializedName("CreateDate")
    var createDate: String? = null,
    @SerializedName("CurrentPage")
    var currentPage: Int? = null,
    @SerializedName("IsSubscriber")
    var isSubscriber: Boolean? = null,
    @SerializedName("Message")
    var message: String? = null,
    @SerializedName("TotalCommentFavorite")
    var totalCommentFavorite: Int? = null,
    @SerializedName("TotalCommentLike")
    var totalCommentLike: Int? = null,
    @SerializedName("TotalReply")
    var totalReply: Int? = null,
    @SerializedName("UserName")
    var userName: String? = null,
    @SerializedName("UserPic")
    var userPic: String? = null
)