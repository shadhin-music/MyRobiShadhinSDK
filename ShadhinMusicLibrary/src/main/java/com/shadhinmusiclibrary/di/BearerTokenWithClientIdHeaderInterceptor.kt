package com.shadhinmusiclibrary.di

import com.shadhinmusiclibrary.data.repository.AuthRepository
import com.shadhinmusiclibrary.utils.AppConstantUtils
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
                    .addHeader("Client", "2")
                    .build()
            )
        }
    }
}