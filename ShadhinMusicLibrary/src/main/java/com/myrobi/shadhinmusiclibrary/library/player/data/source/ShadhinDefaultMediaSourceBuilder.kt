package com.myrobi.shadhinmusiclibrary.library.player.data.source

import android.content.Context
import android.os.Bundle
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.CharParser
import com.myrobi.shadhinmusiclibrary.utils.exH
import com.myrobi.shadhinmusiclibrary.utils.randomString

internal class ShadhinDefaultMediaSourceBuilder(
    private val context: Context,
    private val video: VideoModel,
    private val cache: SimpleCache,
    private val musicRepository: MusicRepository
) : MediaSourceBuilder {

    override fun build(): MediaSource {
        val dataSource: DataSource.Factory =
            ShadhinDataSourceFactory.buildWithoutWriteCache(
                context,
                toMusic(video),
                cache,
                musicRepository
            )
        val pla = toVideoMediaItem(video)
        return ProgressiveMediaSource.Factory(dataSource)
            .createMediaSource(pla)
    }
    private fun toMusic(video: VideoModel): Music {
        return Music(
            mediaId = video.contentID,
            title = video.title,
            displayDescription = "",
            date = "",
            displayIconUrl = CharParser.getImageFromTypeUrl(video.image, video.contentType),
            mediaUrl = video.playUrl,
            artistName = video.artist,
            contentType = video.contentType,
            userPlayListId = video.albumId,
            fav = video.fav,
            trackType = video.trackType,
            isPaid = video.isPaid,
        ).applyRootInfo(
            rootId = video.rootId,
            rootType = video.rootType,
            // rootTitle = video.rootTitle,
            // rootImage = video.rootImage
        )
    }
