package com.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DataSource
import com.shadhinmusiclibrary.di.ShadhinApp
import com.shadhinmusiclibrary.library.player.data.rest.MusicRepository

internal class MyBLDownloadManager(private val context: Context, musicRepository: MusicRepository) {
    var dataSourceFactory: DataSource.Factory =
        DownloadDataSourceFactory.build(context, musicRepository)
    private var dataBase: DatabaseProvider

    //  var downloadCache: Cache
    var downloadManager: DownloadManager

    init {
        dataBase = StandaloneDatabaseProvider(context)
        downloadManager = DownloadManager(
            context,
            dataBase,
            ShadhinApp.module(context).exoplayerCache,
            dataSourceFactory,
            Runnable::run
        )
    }
}