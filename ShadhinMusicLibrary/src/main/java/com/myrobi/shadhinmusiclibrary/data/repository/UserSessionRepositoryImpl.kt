package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.model.UserSessionBody
import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall

import java.util.*

internal class UserSessionRepositoryImpl(private val apiService: ApiService):UserSessionRepository {
    private  var startTime:Date ? = null
    override suspend fun start() {
       startTime = Date()
        Log.i("UserSession", "start: ${startTime.toString()}")
    }

    override suspend fun end() {
        val endTime = Date()
        Log.i("UserSession", "end: ${startTime.toString()} end ${endTime.toString()}")
        if(startTime !=null){
            val body = UserSessionBody.create(startTime!!,endTime)
            safeApiCall { apiService.userSession(body) }
        }
    }
}