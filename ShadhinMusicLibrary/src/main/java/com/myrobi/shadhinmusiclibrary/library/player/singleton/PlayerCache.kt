package com.myrobi.shadhinmusiclibrary.library.player.singleton

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

internal object PlayerCache {
    @Volatile
    private var INSTANCE: SimpleCache? = null
    fun getInstance(context: Context): SimpleCache =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildCache(context).also { INSTANCE = it }
        }

    private fun buildCache(context: Context): SimpleCache {
        //val evictor = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val evictor = NoOpCacheEvictor()
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(File(context.filesDir, "media"), evictor, databaseProvider)
    }
}