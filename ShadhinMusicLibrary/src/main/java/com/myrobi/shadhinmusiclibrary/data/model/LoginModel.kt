package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

@Keep

internal data class LoginModel (
    @SerializedName("msisdn")
    @Expose
    var msisdn: String? = null)






//    @SerializedName("userFullName")
//    @Expose
//    var userFullName: String? = null
//
//    @SerializedName("imageURL")
//    @Expose
//    var imageURL: String? = null
//
//    @SerializedName("gender")
//    @Expose
//    var gender: String? = null
//
//    @SerializedName("deviceToken")
//    @Expose
//    var deviceToken: String? = null
//}