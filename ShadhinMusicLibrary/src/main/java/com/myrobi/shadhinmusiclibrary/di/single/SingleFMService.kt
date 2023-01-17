package com.myrobi.shadhinmusiclibrary.di.single


import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import retrofit2.Retrofit

internal class SingleFMService private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: ApiService? = null
        fun getInstance(fmClient: Retrofit): ApiService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance(fmClient).also { INSTANCE = it }
            }

        private fun newInstance(fmClient: Retrofit): ApiService {
            return fmClient.create(ApiService::class.java)
        }
    }
}