package com.shadhinmusiclibrary.di.single

import com.shadhinmusiclibrary.data.remote.ApiService
import retrofit2.Retrofit

internal class ShadhinMusicAPIService private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: ApiService? = null
        fun getInstance(retrofit: Retrofit): ApiService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance(retrofit).also { INSTANCE = it }
            }

        private fun newInstance(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}