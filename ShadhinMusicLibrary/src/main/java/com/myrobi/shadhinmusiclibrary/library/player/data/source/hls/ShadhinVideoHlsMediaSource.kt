package com.myrobi.shadhinmusiclibrary.library.player.data.source.hls

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.source.MediaSources
import com.myrobi.shadhinmusiclibrary.library.player.data.source.ShadhinDataSourceFactory
import com.myrobi.shadhinmusiclibrary.library.player.utils.CharParser
import com.myrobi.shadhinmusiclibrary.utils.exH
import com.myrobi.shadhinmusiclibrary.utils.randomString

internal class ShadhinVideoHlsMediaSource(
    private val context: Context,
    private val videoList: List<VideoModel>,
    private val cache: SimpleCache,
    private val musicRepository: MusicRepository
) : MediaSources {
    override fun createSources(): List<MediaSource> {
        return videoList.map { createSource(it) }
    }

    private fun createSource(video: VideoModel): MediaSource {

        val dataSource: DataSource.Factory =
            ShadhinDataHlsSourceFactory.buildWithoutWriteCache(
                context,
                toMusic(video),
                cache,
                musicRepository
            )
        val pla = toVideoMediaItem(video)
        return HlsMediaSource.Factory(dataSource)
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

    private fun toVideoMediaItem(video: VideoModel): MediaItem {
        val videoUrl = "${Constants.FILE_BASE_URL}${video.playUrl}"

        return MediaItem.Builder()
            .setMediaId(video.contentID ?: randomString(5))
            .setUri(videoUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(video.title)
                    .setMediaUri(videoUrl.toUri())
                    .setExtras(Bundle().apply {
                        putString(Music.CONTENT_TYPE, video.contentType)
                    })
                    .setArtworkUri(exH { video.image?.toUri() })
                    .setArtist(video.artist)
                    .build()
            ).build()
    }
}