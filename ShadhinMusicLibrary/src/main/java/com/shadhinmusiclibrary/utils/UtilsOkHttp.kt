package com.shadhinmusiclibrary.utils

import com.shadhinmusiclibrary.di.BearerTokenWithClientIdHeaderInterceptor
import com.shadhinmusiclibrary.di.ClientIdHeaderInterceptor
import com.shadhinmusiclibrary.di.single.BearerTokenHeaderInterceptor
import com.shadhinmusiclibrary.fragments.artist.LastFMApiKeyHeaderInterceptor
import okhttp3.OkHttpClient

internal object UtilsOkHttp {
    fun getOkHttpClientWithFMInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                LastFMApiKeyHeaderInterceptor()
            ).build()
    }

    fun getBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                ClientIdHeaderInterceptor()
            ).build()
    }

    @Deprecated("Only Token not have client ID")
    fun getBaseOkHttpClientWithToken(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                BearerTokenHeaderInterceptor()
            ).build()
    }

    fun getBaseOkHttpClientWithTokenAndClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                BearerTokenWithClientIdHeaderInterceptor()
            ).build()
    }
}