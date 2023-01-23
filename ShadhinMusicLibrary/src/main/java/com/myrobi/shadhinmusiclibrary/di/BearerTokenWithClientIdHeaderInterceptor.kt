package com.myrobi.shadhinmusiclibrary.di


import com.myrobi.shadhinmusiclibrary.data.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response

internal class BearerTokenWithClientIdHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val finalToken = ("Bearer " + if (AuthRepository.appToken != null) {
            AuthRepository.appToken
        } else {
            AuthRepository.appToken
        })
        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", finalToken)
                    .addHeader("Client", "4")
                    .build()
            )
        }
    }
}