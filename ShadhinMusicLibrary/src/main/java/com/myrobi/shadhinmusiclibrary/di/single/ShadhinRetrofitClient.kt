package com.myrobi.shadhinmusiclibrary.di.single


import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class ShadhinRetrofitClient private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null
        fun getInstance(): Retrofit =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: newInstance().also { INSTANCE = it }
            }

        private fun newInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(AppConstantUtils.BASE_URL_API_shadhinmusic)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}