package com.myrobi.shadhinmusiclibrary.library.player.data.source.hls

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.source.PlayerInterceptor
import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo
import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.isDataSourceError
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


private const val TAG = "DataSourceFactory"

internal open class ShadhinDataHlsSourceFactory constructor(
    private val context: Context,
    private val music: Music,
    private val musicRepository: MusicRepository
) : DataSource.Factory {
    private lateinit var factory: DataSource.Factory

    init {
        initialization()
    }

    private fun initialization() {
        isDataSourceError = false
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(Interceptor { chain ->
                DataSourceInfo.isDataSourceError = false
                val newUrl = musicRepository.fetchURL(music)
             //   chain.request()
               /* val newRequest =
                    chain.request().newBuilder()
                        .url(newUrl)
                        .header("User-Agent", Constants.userAgent)
                        .method("GET", null)
                        .build()

                Log.i(TAG, "initialization: ${chain.request().url.encodedFragment}")*/
                chain.proceed(chain.request())
            })
            .build()


        val networkFactory = OkHttpDataSource.Factory(client)
        factory = DefaultDataSource.Factory(context, networkFactory)
    }

    override fun createDataSource(): DataSource {
        Log.i("ShadhinVediaSource", "createSource: ${Thread.currentThread().name}")
        return factory.createDataSource()
    }

    companion object {
        @JvmStatic
        fun build(
            context: Context,
            music: Music,
            cache: SimpleCache,
            musicRepository: MusicRepository
        ): DataSource.Factory {

            return CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(
                    ShadhinDataHlsSourceFactory(context, music, musicRepository)
                )
                // TODO must be remove setCacheWriteDataSinkFactory(null) this line when download done . but this time for testing
                // .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        }

        @JvmStatic
        fun buildWithoutWriteCache(
            context: Context,
            music: Music,
            cache: SimpleCache,
            musicRepository: MusicRepository
        ): DataSource.Factory {
            return CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(
                    ShadhinDataHlsSourceFactory(context, music, musicRepository)
                )
                // TODO must be remove setCacheWriteDataSinkFactory(null) this line when download done . but this time for testing
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }
    }
}