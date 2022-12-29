package com.shadhinmusiclibrary.fragments.artist


import kotlin.Throws
import com.shadhinmusiclibrary.utils.AppConstantUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal class LastFMApiKeyHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val url = request.url
            .newBuilder()
            .addQueryParameter("api_key", AppConstantUtils.LAST_FM_API_KEY)
            .addQueryParameter("format", AppConstantUtils.LAST_FM_CONTENT_TYPE_JSON)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}