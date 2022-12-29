package com.shadhinmusiclibrary.library.player.listener

import android.content.Context
import android.os.Looper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.shadhinmusiclibrary.library.player.ShadhinMusicPlaybackPreparer
import com.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.shadhinmusiclibrary.library.player.data.rest.user_history.UserHistoryRepository
import com.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.dataSourceErrorCode
import com.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.dataSourceErrorMessage
import com.shadhinmusiclibrary.library.player.singleton.DataSourceInfo.isDataSourceError
import com.shadhinmusiclibrary.library.player.utils.PlayerLogSender
import com.shadhinmusiclibrary.library.player.utils.isLocalUrl
import kotlinx.coroutines.*

internal class ShadhinPlayerListener(
    private val serviceScope: CoroutineScope?,
    private val context: Context,
    private val musicPlaybackPreparer: ShadhinMusicPlaybackPreparer?,
    private val musicRepository: MusicRepository,
    private val userHistoryRepository: UserHistoryRepository,
) : Player.Listener, AnalyticsListener {
    // private var localStorage: LocalStorage = PreferenceStorage(context)
    private var liveErrorHandler: android.os.Handler? = null
    private var previousJob: Job? = null
    private val playerLogSender: PlayerLogSender =
        PlayerLogSender(serviceScope, userHistoryRepository)

    init {
        musicPlaybackPreparer?.unsubscribeListener(::unsubscribe)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super<Player.Listener>.onMediaItemTransition(mediaItem, reason)
        playerLogSender.mediaItemTransition(mediaItem, reason)
        //  initPreviousSeekPosition(mediaItem,reason)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super<Player.Listener>.onIsPlayingChanged(isPlaying)
        if (!isPlaying) {
            refreshStreamingStatusAsync()
        }
        playerLogSender.playingChanged(isPlaying)
        saveSeekPosition(isPlaying)
    }

    override fun onPositionDiscontinuity(
        eventTime: AnalyticsListener.EventTime,
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        super<AnalyticsListener>.onPositionDiscontinuity(
            eventTime,
            oldPosition,
            newPosition,
            reason
        )
        /* exH {
             val music = musicPlaybackPreparer?.musicAt(oldPosition.mediaItemIndex)
             val positionMs = oldPosition.positionMs
             if(reason == DISCONTINUITY_REASON_SEEK && positionMs !=0L) {
                 serviceScope?.launch {
                     musicPositionRepository?.savePosition(music = music, position = positionMs)
                 }
             }
         }*/
    }

    fun refreshStreamingStatus() = runBlocking {
        //  if(localStorage.getToken().isValidToken()) {
        musicRepository.refreshStreamingStatus()
        //  userApiService?.refreshStreamingStatus(localStorage.getToken())
        //   }
    }

    private fun refreshStreamingStatusAsync() = serviceScope?.launch {
        //if(localStorage.getToken().isValidToken()) {
        // userApiService?.refreshStreamingStatus(localStorage.getToken())
        musicRepository.refreshStreamingStatus()
        // }
    }

    fun unsubscribe() {
        /*exH {
            val music = musicPlaybackPreparer?.currentMusic()
            val position = musicPlaybackPreparer?.playerProgress()?.currentPosition
            serviceScope?.launch {
                musicPositionRepository?.savePosition(music = music, position = position)
            }

        }*/
    }

    public suspend fun playerClose() {
        withTimeout(1000 * 2) {
            playerLogSender.closePlayer()
        }
    }

    private fun saveSeekPosition(playing: Boolean) {
        /*  if(playing) {
              exH {
                  val music = musicPlaybackPreparer?.currentMusic()
                  val duration = musicPlaybackPreparer?.playerProgress()?.duration
                  serviceScope?.launch {
                      musicPositionRepository?.savePosition(music = music, duration = duration)
                  }
              }
          }*/
    }

    /*private fun initPreviousSeekPosition(mediaItem: MediaItem?, reason: Int) {
        val mediaId = mediaItem?.mediaId
        previousJob?.cancel()
        previousJob = serviceScope?.launch {
            delay(500)
            val obj = musicPositionRepository?.getSavedPosition(mediaId)
            val savedPosition = obj?.position

            withContext(Dispatchers.Main){
                savedPosition?.let {
                    musicPlaybackPreparer?.seekTo(it)
                }
            }
        }
    }*/

    override fun onPlaybackStateChanged(playbackState: Int) {
        super<Player.Listener>.onPlaybackStateChanged(playbackState)
        if (playbackState == ExoPlayer.STATE_ENDED) {
            if (musicPlaybackPreparer?.isLive() == true) {
                handleLive()
            }
            refreshStreamingStatusAsync()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super<Player.Listener>.onPlayerError(error)
        val currentMusic = musicPlaybackPreparer?.currentMusic()
        if (error.errorCode == 2005 && currentMusic?.mediaUrl.isLocalUrl()) {
            musicPlaybackPreparer?.sendError(
                true,
                "File not found maybe you are cleared cache",
                2005,
                currentMusic
            )
            return
        }

        val errorMessage = if (isDataSourceError) dataSourceErrorMessage else error.localizedMessage
        musicPlaybackPreparer?.sendError(
            isDataSourceError,
            errorMessage,
            dataSourceErrorCode,
            currentMusic
        )

        if (musicPlaybackPreparer?.isLive() == true) {
            handleLive()
        }
    }

    private fun handleLive() {
        if (liveErrorHandler != null) {
            liveErrorHandler?.removeCallbacksAndMessages(null)
        }
        if (liveErrorHandler == null) {
            liveErrorHandler = android.os.Handler(Looper.getMainLooper())
        }
        liveErrorHandler?.postDelayed({
            musicPlaybackPreparer?.restartPlayer()
        }, 3000)
    }
}