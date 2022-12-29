package com.shadhinmusiclibrary.data.remote

import com.shadhinmusiclibrary.data.model.auth.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Header

internal interface ApiLoginService {
    @GET("mybl/Login")
    suspend fun login(@Header("Authorization") token: String): LoginResponse
}