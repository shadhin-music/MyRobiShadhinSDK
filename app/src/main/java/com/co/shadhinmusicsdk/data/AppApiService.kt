package com.co.shadhinmusicsdk.data

import com.co.shadhinmusicsdk.data.model.LoginModel
import com.co.shadhinmusicsdk.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AppApiService {
    @POST("mybl/gettoken")
    fun fetchMyBlLogin(@Body loginData: LoginModel): Call<LoginResponse>
}