package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository

internal class ShadhinMediaSourceBuilderFactory(
    private val context: Context,
    private val video: VideoModel,
    private val cache: SimpleCache,
    private val musicRepository: MusicRepository
) {
    fun mediaSourceBuilder():MediaSourceBuilder {
        return if(isHls()){
            ShadhinHlsMediaSourceBuilder(context,
                video,
                musicRepository
            )

        }else{
            ShadhinDefaultMediaSourceBuilder(
                context,
                video,
                cache,
                musicRepository
            )
        }
    }
    private fun isHls() = video.playUrl?.contains(".m3u8",true)?:false
}