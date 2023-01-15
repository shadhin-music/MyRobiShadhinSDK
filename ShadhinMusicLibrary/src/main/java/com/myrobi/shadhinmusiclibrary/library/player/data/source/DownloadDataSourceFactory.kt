package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository

import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.isDataSourceError
import okhttp3.OkHttpClient


private const val TAG = "DownloadDataSourceFactory"

internal open class DownloadDataSourceFactory private constructor(
    private val context: Context,
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
            .addInterceptor(DownloadInterceptor(musicRepository))
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
            musicRepository: MusicRepository
        ): DataSource.Factory {
            return DownloadDataSourceFactory(context, musicRepository)
        }
    }
}