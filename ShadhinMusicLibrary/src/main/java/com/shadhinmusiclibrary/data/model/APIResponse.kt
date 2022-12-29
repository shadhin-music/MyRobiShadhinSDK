package com.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class APIResponse<ResultType>(
    val data: ResultType,
    val fav: Any,
    val follow: Any,
    val image: String,
    val message: String,
    val status: String,
    val total: Int,
    val type: Any,
    val name:String,
    val albumName:String,
    val artistName:String,
    val artistId:String,
    val albumImage:String,


)