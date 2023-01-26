package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.util.Log
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo
import okhttp3.Interceptor
import okhttp3.Response

internal class PlayerHlsInterceptor(
    private val musicRepository: MusicRepository,
    private val music: Music
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        DataSourceInfo.isDataSourceError = false

        val firstUrl = "${Constants.FILE_BASE_URL}${music.filePath()}"
        val currentUrl = chain.request().url.toString()

        val finalUrl = if (currentUrl == firstUrl)
            musicRepository.fetchURL(music)
        else
            currentUrl

        val newRequest =
            chain.request().newBuilder()
                .url(finalUrl)
                .header("User-Agent", Constants.userAgent)
                .method("GET", null)
                .build()
        return chain.proceed(newRequest)
    }
}