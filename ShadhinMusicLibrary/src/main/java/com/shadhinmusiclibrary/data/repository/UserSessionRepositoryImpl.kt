package com.shadhinmusiclibrary.data.repository

import android.util.Log
import com.shadhinmusiclibrary.data.model.UserSessionBody
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall
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