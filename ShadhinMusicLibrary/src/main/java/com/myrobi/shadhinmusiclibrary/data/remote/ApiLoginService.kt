package com.myrobi.shadhinmusiclibrary.data.remote


import com.myrobi.shadhinmusiclibrary.data.model.LoginModel
import com.myrobi.shadhinmusiclibrary.data.model.auth.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface ApiLoginService {
//    @GET("mybl/Login")
//    suspend fun login(@Header("Authorization") token: String): LoginResponse
    @POST("ROBI/Login")
    suspend fun fetchMyRobiLogin(@Body loginData: LoginModel):LoginResponse
//    @POST("ROBI/Login")
//    suspend fun login(): LoginResponse
//    @POST("ROBI/Login")
//    fun fetchMyRobiLogin(@Body loginData: LoginModel): Call<LoginResponse>
}