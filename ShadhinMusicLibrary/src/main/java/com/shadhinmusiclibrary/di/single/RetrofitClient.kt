package com.shadhinmusiclibrary.di.single

import com.shadhinmusiclibrary.utils.AppConstantUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class RetrofitClient private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null
        fun getInstance(client: OkHttpClient): Retrofit =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance(client).also { INSTANCE = it }
            }

        private fun newInstance(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(AppConstantUtils.BASE_URL_API_shadhinmusic)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}