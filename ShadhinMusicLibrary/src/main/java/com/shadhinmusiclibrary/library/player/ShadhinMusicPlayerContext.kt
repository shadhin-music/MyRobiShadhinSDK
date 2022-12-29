package com.shadhinmusiclibrary.library.player


import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.CoroutineScope

internal interface ShadhinMusicPlayerContext: MediaContext {
    val musicPlaybackPreparer: ShadhinMusicPlaybackPreparer?
    val playerCache: SimpleCache?
}
internal interface MediaContext{
    val exoPlayer: ExoPlayer?
    val scope: CoroutineScope?
}