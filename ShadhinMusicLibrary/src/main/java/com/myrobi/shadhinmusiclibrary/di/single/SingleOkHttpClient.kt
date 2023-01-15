package com.myrobi.shadhinmusiclibrary.di.single


import com.myrobi.shadhinmusiclibrary.fragments.artist.LastFMApiKeyHeaderInterceptor
import okhttp3.OkHttpClient

internal class SingleOkHttpClient private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: OkHttpClient? = null
        fun getInstance(): OkHttpClient =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance().also { INSTANCE = it }
            }

        private fun newInstance(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(LastFMApiKeyHeaderInterceptor()).build()
        }
    }
}