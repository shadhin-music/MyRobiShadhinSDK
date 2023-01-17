package com.myrobi.shadhinmusiclibrary.utils

import com.myrobi.shadhinmusiclibrary.di.BearerTokenWithClientIdHeaderInterceptor
import com.myrobi.shadhinmusiclibrary.di.ClientIdHeaderInterceptor
import com.myrobi.shadhinmusiclibrary.di.single.BearerTokenHeaderInterceptor
import com.myrobi.shadhinmusiclibrary.fragments.artist.LastFMApiKeyHeaderInterceptor
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