package com.co.shadhinmusicsdk.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse : Serializable {
    @SerializedName("Message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: LoginData? = null
}

class LoginData : Serializable {
    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null

    @SerializedName("tokenType")
    @Expose
    var tokenType: String? = null

    @SerializedName("expire")
    @Expose
    var expire: Long? = null
}