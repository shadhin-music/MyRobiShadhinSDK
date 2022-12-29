package com.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.shadhinmusiclibrary.library.player.data.model.Music
import com.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.isDataSourceError
import okhttp3.OkHttpClient


private const val TAG = "DataSourceFactory"

internal open class ShadhinDataSourceFactory constructor(
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
            .addInterceptor(PlayerInterceptor(musicRepository, music))
            .build()


        val networkFactory = OkHttpDataSource.Factory(client)
        factory = DefaultDataSource.Factory(context, networkFactory)
    }

    override fun createDataSource(): DataSource {
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
                    ShadhinDataSourceFactory(context, music, musicRepository)
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
                    ShadhinDataSourceFactory(context, music, musicRepository)
                )
                // TODO must be remove setCacheWriteDataSinkFactory(null) this line when download done . but this time for testing
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }
    }
}