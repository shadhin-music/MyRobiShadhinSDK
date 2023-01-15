package com.myrobi.shadhinmusiclibrary.di

import okhttp3.Interceptor
import okhttp3.Response

internal class ClientIdHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
//                .addHeader("Client", "2")
                .build()
        )
    }
}