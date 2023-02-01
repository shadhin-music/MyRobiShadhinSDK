package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.isDataSourceError
import okhttp3.OkHttpClient


private const val TAG = "DataSourceFactory"

internal open class ShadhinHlsDataSourceFactory constructor(
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
            .addInterceptor(PlayerHlsInterceptor(musicRepository, music))
            .build()

        factory = OkHttpDataSource.Factory(client)
    }

    override fun createDataSource(): DataSource {
        return factory.createDataSource()
    }

    companion object {
        @JvmStatic
        fun build(
            context: Context,
            music: Music,
            musicRepository: MusicRepository
        ): DataSource.Factory {
            return ShadhinHlsDataSourceFactory(context, music, musicRepository)
        }

    }
}