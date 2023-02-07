package com.co.robishadhinmusicsdk.data.model

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientService {

    fun getRetrofitBaseService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://salute.gakktech.com/api/v5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder().apply {
                            addHeader("Client", "TxECGqgk27aql91eRpdFlg==")
                            addHeader("ClientKey", "72w/mQtr5ttRaZJULcZ/Fg==")
                            addHeader("RequestType", "Login")
//                            addHeader("Client", "e80kOMj/1lRKMfFY4xPGWw==")
//                            addHeader("ClientKey", "7+bdGKVhqs7sz/anPQigbA==")
//                            addHeader("RequestType", "TokenRetrieval")
                        }.build()
                    )
                }).build()
            ).build()
    }
}