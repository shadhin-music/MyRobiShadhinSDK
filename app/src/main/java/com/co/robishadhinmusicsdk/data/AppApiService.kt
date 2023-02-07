package com.co.robishadhinmusicsdk.data


import com.co.robishadhinmusicsdk.data.model.LoginModel
import com.co.robishadhinmusicsdk.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AppApiService {
//    @POST("mybl/gettoken")
//    fun fetchMyBlLogin(@Body loginData: LoginModel): Call<LoginResponse>
    @POST("ROBI/Login")
    fun fetchMyRobiLogin(@Body loginData: LoginModel): Call<LoginResponse>
}