package com.co.shadhinmusicsdk.data.model

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientService {

    fun getRetrofitBaseService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.shadhinmusic.com/api/v5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder().apply {
                            addHeader("Client", "e80kOMj/1lRKMfFY4xPGWw==")
                            addHeader("ClientKey", "7+bdGKVhqs7sz/anPQigbA==")
                            addHeader("RequestType", "TokenRetrieval")
                        }.build()
                    )
                }).build()
            ).build()
    }
}