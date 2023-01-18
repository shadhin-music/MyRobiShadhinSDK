package com.myrobi.shadhinmusiclibrary.di

import okhttp3.Interceptor
import okhttp3.Response

internal class ClientIdRequestIdHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Client", "TxECGqgk27aql91eRpdFlg==")
                .addHeader("ClientKey", "72w/mQtr5ttRaZJULcZ/Fg==")
                .addHeader("RequestType", "Login")
                .build()
        )
    }
}