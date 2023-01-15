package com.myrobi.shadhinmusiclibrary.library.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.myrobi.shadhinmusiclibrary.library.player.Constants.MEDIA_SESSION_TAG
import com.myrobi.shadhinmusiclibrary.library.player.Constants.PENDING_INTENT_KEY
import com.myrobi.shadhinmusiclibrary.library.player.Constants.PENDING_INTENT_REQUEST_CODE
import com.myrobi.shadhinmusiclibrary.library.player.Constants.ROOT_ID_EMPTY
import com.myrobi.shadhinmusiclibrary.library.player.Constants.ROOT_ID_PLAYLIST

import com.myrobi.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.myrobi.shadhinmusiclibrary.library.player.listener.ShadhinMusicPlayerNotificationListener
import com.myrobi.shadhinmusiclibrary.library.player.listener.ShadhinPlayerListener
import com.myrobi.shadhinmusiclibrary.library.player.utils.toServiceMediaItemMutableList

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.activities.SDKMainActivity
import com.myrobi.shadhinmusiclibrary.di.ServiceEntryPoint
import com.myrobi.shadhinmusiclibrary.library.player.connection.ShadhinMusicServiceConnection
import com.myrobi.shadhinmusiclibrary.utils.exH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


/**
 * Shadhin Music Service
 * <img src="https://developer.android.com/guide/topics/media-apps/images/audio-activity-and-service.png"/>
 */
private const val TAG = "ShadhinMusicPlayer"

internal class ShadhinMusicPlayer : MediaBrowserServiceCompat(),
    ShadhinMusicPlayerContext,
    ServiceEntryPoint {

    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null
    private var shadhinMusicNotificationManager: ShadhinMusicNotificationManager? = null
    private var shadhinMusicQueueNavigator: ShadhinMusicQueueNavigator? = null
    private var musicPlayerNotificationListener: ShadhinMusicPlayerNotificationListener? = null
    private var shadhinPlayerListener: ShadhinPlayerListener? = null
    override var exoPlayer: ExoPlayer? = null
    override var musicPlaybackPreparer: ShadhinMusicPlaybackPreparer? = null
    override var scope: CoroutineScope? = null
    override var playerCache: SimpleCache? = null

    var isForegroundService = false

    override fun onCreate() {
        super.onCreate()
        scope = CoroutineScope(Dispatchers.IO)
        scope?.launch(Dispatchers.Main) {
            kotlin.runCatching { initialization() }
            kotlin.runCatching { eventLogAndLiveHandle() }
        }
    }

    private suspend fun eventLogAndLiveHandle() {
        shadhinPlayerListener = ShadhinPlayerListener(
            scope,
            this,
            musicPlaybackPreparer,
            injector.musicRepository,
            injector.userHistoryRepository
        )
        shadhinPlayerListener?.let {
            exoPlayer?.addListener(it)
            exoPlayer?.addAnalyticsListener(it)
        }
    }

    private suspend fun initialization() {
        exoPlayer = ShadhinExoPlayer.build(this)
        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG).apply {
            //  setSessionActivity(pendingIntent())
            isActive = true

        }
        sessionToken = mediaSession?.sessionToken
        musicPlayerNotificationListener =
            ShadhinMusicPlayerNotificationListener(this@ShadhinMusicPlayer)
        mediaSessionConnector = mediaSession?.let { MediaSessionConnector(it) }
        shadhinMusicNotificationManager = ShadhinMusicNotificationManager(
            this,
            mediaSession?.sessionToken,
            musicPlayerNotificationListener
        )
        musicPlaybackPreparer =
            ShadhinMusicPlaybackPreparer(
                this@ShadhinMusicPlayer,
                exoPlayer,
                injector.exoplayerCache,
                injector.musicRepository
            )
        shadhinMusicQueueNavigator = mediaSession?.let { ShadhinMusicQueueNavigator(it) }
        mediaSessionConnector?.setQueueNavigator(shadhinMusicQueueNavigator)
        mediaSessionConnector?.setPlayer(exoPlayer)
        mediaSessionConnector?.setPlaybackPreparer(musicPlaybackPreparer)
    }

    private suspend fun pendingIntent(): PendingIntent? {
        val intent = Intent(this, SDKMainActivity::class.java)
        intent.putExtra(PENDING_INTENT_KEY, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            this,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        //isFirstTime = true
        /*musicPositionRepository?.savePositionOnMainThread(
           music =  exoPlayer?.currentMediaItem?.toMusic(),
           position = exoPlayer?.currentPosition
        )*/
        musicPlaybackPreparer?.removeTimerHandler()
        shadhinPlayerListener?.let { exoPlayer?.removeListener(it) }

        exoPlayer?.clearMediaItems()
        exoPlayer?.stop()
        shadhinMusicNotificationManager?.hideNotification()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        //isFirstTime = true

        musicPlaybackPreparer?.removeTimerHandler()
        mediaSession?.release()
        exoPlayer?.release()
        exoPlayer = null

        scope?.launch {
            shadhinPlayerListener?.playerClose()
            shadhinPlayerListener?.refreshStreamingStatus()
            scope?.cancel()
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {

        //  startForeground()
        return if (clientPackageName == applicationContext.packageName)
            return BrowserRoot(ROOT_ID_PLAYLIST, null)
        else BrowserRoot(ROOT_ID_EMPTY, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
        options: Bundle
    ) {
        val musicPlayList = MusicPlayList.fromBundle(
            options,
            ShadhinMusicServiceConnection.Command.SUBSCRIBE
        )
        val isPlayWhenReady = options.getBoolean(Constants.PLAY_WHEN_READY_KEY)
        val isUpdatePlaylist = options.getBoolean(Constants.PLAYLIST_UPDATE)
        val defaultPosition = options.getInt(Constants.DEFAULT_POSITION_KEY)
        exH { sendData(musicPlayList, result, isPlayWhenReady, defaultPosition) }
    }

    private fun sendData(
        musicPlayList: MusicPlayList,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
        isPlayWhenReady: Boolean = false,
        defaultPosition: Int
    ) {
        shadhinMusicNotificationManager?.showNotification(exoPlayer)
        if (musicPlayList.isValidPlayList()) {
            //musicPlayList.loadAllImage(this)
            musicPlaybackPreparer?.defaultPosition = defaultPosition
            musicPlaybackPreparer?.initPlayList(musicPlayList)
            musicPlaybackPreparer?.onPrepare(isPlayWhenReady)
            result.sendResult(musicPlayList.list.toServiceMediaItemMutableList() ?: mutableListOf())
        } else {
            result.sendResult(mutableListOf())
        }
    }

    override fun onLoadChildren(
        parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(mutableListOf())
    }
}