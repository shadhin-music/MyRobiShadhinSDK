package com.shadhinmusiclibrary.di.single

import com.shadhinmusiclibrary.utils.AppConstantUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class RetrofitFMClient private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null
        fun getInstance(okHttpClient: OkHttpClient): Retrofit =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance(okHttpClient).also { INSTANCE = it }
            }

        private fun newInstance(okHttpClient: OkHttpClient):Retrofit{
            return Retrofit.Builder()
                .baseUrl(AppConstantUtils.LAST_FM_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        fun destroy(){
           INSTANCE = null
        }
    }
}