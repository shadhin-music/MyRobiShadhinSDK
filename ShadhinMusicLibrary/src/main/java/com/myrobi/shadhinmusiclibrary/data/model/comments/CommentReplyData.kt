package com.myrobi.shadhinmusiclibrary.data.model.comments

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
internal data class CommentReplyData(
    @SerializedName("ReplyId")
    private var replyId: Int? = null,
    @SerializedName("CommentId")
    var commentId: Int? = null,

    @SerializedName("UserName")
    var userName: String? = null,

    @SerializedName("UserPic")
    var userPic: String? = null,

    @SerializedName("ReplyLike")
    var replyLike: Boolean? = null,

    @SerializedName("TotalReplyLike")
    var totalReplyLike: Int? = null,

    @SerializedName("ReplyFavorite")
    var replyFavorite: Boolean? = null,

    @SerializedName("TotalReplyFavorite")
    var totalReplyFavorite: Int? = null,

    @SerializedName("Message")
    var message: String? = null,

    @SerializedName("CreateDate")
    var createDate: String? = null,

    @SerializedName("AdminUserType")
    var userType: String? = null,
)
