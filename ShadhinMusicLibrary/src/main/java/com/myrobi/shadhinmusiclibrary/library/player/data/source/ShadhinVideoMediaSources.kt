package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository

internal class ShadhinVideoMediaSources(
    private val context: Context,
    private val videoList: List<VideoModel>,
    private val cache: SimpleCache,
    private val musicRepository: MusicRepository
) : MediaSources {
    override fun createSources(): List<MediaSource> {
        return videoList.map { video ->
            ShadhinMediaSourceBuilderFactory(
                context,
                video,
                cache,
                musicRepository
            ).mediaSourceBuilder().build()
        }
    }
}