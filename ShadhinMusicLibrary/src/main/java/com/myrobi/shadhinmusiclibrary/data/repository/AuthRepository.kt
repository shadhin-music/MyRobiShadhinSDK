package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.model.LoginModel
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class AuthRepository(private val apiLoginSer: ApiLoginService) {
//    suspend fun login(): Pair<Boolean, String?> {
//
//            val response = safeApiCall { apiLoginSer.fetchMyRobiLogin(LoginModel()
//                .apply {
//                    msisdn = "8801861901236"
//                })}
//        return if (response.status == Status.SUCCESS) {
//            appToken = response.data.toString()
//            Log.e("AuthRepository: ALS", "login: $appToken")
//            Pair(true, response.message)
//        } else {
//            Log.e("RAA", "onResponse: " + response.message)
//            appToken = null
//            Pair(false, response.message)
//        }
//    }


    suspend fun login(token: String): Pair<Boolean, String?> {
        val response = safeApiCall { apiLoginSer.login("Bearer $token") }
        return if (response.status == Status.SUCCESS) {
            appToken = response.data?.data?.Token
            Log.e("AuthRepository: ALS", "login: $appToken")
            Pair(true, response.message)
        } else {
            Log.e("RAA", "onResponse: " + response.message)
            appToken = null
            Pair(false, response.message)
        }
    }

    companion object {
        var appToken: String? = null
    }
}