package com.myrobi.shadhinmusiclibrary.library.player.data.source

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
    private var hlsUrl:String?=null
    override fun intercept(chain: Interceptor.Chain): Response {
        DataSourceInfo.isDataSourceError = false
        if(hlsUrl == null) {
            hlsUrl = musicRepository.fetchURL(music)
        }


        val newRequest =
            chain.request().newBuilder()
                .url(hlsUrl!!)
                .header("User-Agent", Constants.userAgent)
                .method("GET", null)
                .build()
        return chain.proceed(newRequest)
    }
}