package com.shadhinmusiclibrary.library.player

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.shadhinmusiclibrary.library.player.connection.ShadhinMusicServiceConnection.Command
import com.shadhinmusiclibrary.library.player.data.model.ErrorMessage
import com.shadhinmusiclibrary.library.player.data.model.Music
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.library.player.data.model.PlayerProgress
import com.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.shadhinmusiclibrary.library.player.data.source.MediaSources
import com.shadhinmusiclibrary.library.player.data.source.ShadhinMediaSources
import com.shadhinmusiclibrary.library.player.utils.toMusic
import com.shadhinmusiclibrary.utils.exH
import com.shadhinmusiclibrary.utils.normalize
import java.util.*

internal typealias PlayListUpdateFunc = (position: Int) -> Unit
internal typealias ErrorCallbackFunc = (isDataSourceError: Boolean, message: String?, errorCode: Int?, currentMusic: Music?) -> Unit
internal typealias UnsubscribeFunc = () -> Unit

internal class ShadhinMusicPlaybackPreparer(
    private val context: Context,
    private val exoPlayer: ExoPlayer?,
    private val exoplayerCache: SimpleCache,
    private val musicRepository: MusicRepository
) : MediaSessionConnector.PlaybackPreparer {
    private val concatenatingMediaSource = ConcatenatingMediaSource()
    private var playListUpdateFunc: PlayListUpdateFunc? = null
    private var unsubscribeFunc: UnsubscribeFunc? = null
    private var errorCallbackFunc: ErrorCallbackFunc? = null
    private val playList: MutableList<Music> = ArrayList()
    private var timerHandler: Handler? = null
    var defaultPosition: Int = 0
    private var sleepTimeMillis: Long = Long.MAX_VALUE
    private var startTime: Date? = null

    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean {
        when (command) {
            Command.ADD_PLAYLIST.tag -> addPlayListCommand(extras, cb)
            Command.CHANGE_MUSIC.tag -> changeMusicCommand(extras)
            Command.MUSIC_PROGRESS_REQUEST.tag -> progressRequestCommand(cb)
            Command.STOP_SERVICE.tag -> stop()
            Command.GET_PLAYLIST.tag -> commandPlayListGet(cb)
            Command.PLAYER_SPEED.tag -> setPlayerSpeed(extras)
            Command.SLEEP_TIMER.tag -> setSleepTime(extras)
            Command.GET_SLEEP_TIME.tag -> getSleepTime(cb)
            Command.ERROR_CALLBACK.tag -> errorCallback(cb)
            Command.RE_ASSIGN_CALLBACK.tag -> reAssignAll(cb)
            Command.UNSUBSCRIBE.tag -> unsubscribe()
        }
        return true
    }

    private fun reAssignAll(cb: ResultReceiver?) {
        exH {
            cb?.send(Command.RE_ASSIGN_CALLBACK.resultCode,
                Bundle().apply {
                    putSerializable(
                        Command.RE_ASSIGN_CALLBACK.dataKey,
                        exoPlayer?.currentMediaItem?.toMusic()
                    )
                    putSerializable(
                        Command.RE_ASSIGN_CALLBACK.dataKey3,
                        MusicPlayList(playList.toList(), defaultPosition)
                    )
                }
            )
        }
    }

    private fun unsubscribe() {
        this.unsubscribeFunc?.invoke()
    }

    fun unsubscribeListener(unsubscribeFunc: UnsubscribeFunc?) {
        this.unsubscribeFunc = unsubscribeFunc
    }

    private fun getSleepTime(cb: ResultReceiver?) {
        cb?.send(Command.GET_SLEEP_TIME.resultCode, Bundle().apply {
            putLong(Command.GET_SLEEP_TIME.dataKey, sleepTimeMillis)
            putString(Command.GET_SLEEP_TIME.dataKey2, startTime?.normalize())
        })
    }

    private fun setSleepTime(extras: Bundle?) {
        val isStart = extras?.getBoolean(Command.SLEEP_TIMER.tag2)
        val time = extras?.getLong(Command.SLEEP_TIMER.tag) ?: 0L
        removeTimerHandler()
        if (timerHandler == null) {
            timerHandler = Handler(Looper.getMainLooper())
        }

        this.sleepTimeMillis = -1
        if (isStart == true) {
            startTime = Date()
            timerHandler?.postDelayed({
                exoPlayer?.pause()
                this.sleepTimeMillis = -1
            }, time)
            this.sleepTimeMillis = time
        }
    }

    fun removeTimerHandler() {
        if (timerHandler != null) {
            timerHandler?.removeCallbacksAndMessages(null)
        }
    }

    private fun setPlayerSpeed(extras: Bundle?) {
        val speed = extras?.getFloat(Command.PLAYER_SPEED.dataKey)
        val param = PlaybackParameters(speed ?: 1.0f)
        exoPlayer?.playbackParameters = param
    }

    private fun commandPlayListGet(cb: ResultReceiver?) {
        val command = Command.GET_PLAYLIST
        playListUpdateFunc = { position ->
            cb?.send(command.resultCode, MusicPlayList(playList, position).toBundle(command))
        }
        cb?.send(command.resultCode, null)
    }

    private fun errorCallback(cb: ResultReceiver?) {
        errorCallbackFunc = { isDataSourceError, message, errorCode, currentMusic ->
            cb?.send(
                Command.ERROR_CALLBACK.resultCode,
                ErrorMessage(isDataSourceError, message, errorCode, currentMusic).toBundle()
            )
        }
    }

    fun stop() {
        removeTimerHandler()
        exoPlayer?.clearMediaItems()
        exoPlayer?.stop()
    }

    private fun progressRequestCommand(resultReceiver: ResultReceiver?) {
        reAssignAll(resultReceiver)
        val command = Command.MUSIC_PROGRESS_REQUEST
        val musicPosition = exoPlayer?.let { PlayerProgress.fromPlayer(it) }
        exH { resultReceiver?.send(command.resultCode, musicPosition?.toBundle(command.dataKey)) }
    }

    private fun changeMusicCommand(extras: Bundle?) {
        val position = extras?.getInt(Command.CHANGE_MUSIC.dataKey)
        position?.let { exoPlayer?.seekTo(it, 0L) }
    }

    private fun addPlayListCommand(extras: Bundle?, cb: ResultReceiver?) {
        val command = Command.ADD_PLAYLIST
        val serializable = serializableObject(extras, command)
        if (serializable is MusicPlayList) {
            if (serializable.isValidPlayList()) {
                addPlaylist(serializable)
                cb?.send(command.resultCode, Bundle().apply {
                    putInt(command.dataKey, playList.size)
                })
            }
        }
    }

    private fun serializableObject(extras: Bundle?, command: Command) = extras?.getSerializable(
        command.dataKey
    )

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    fun restartPlayer() {
        if (!playList.isNullOrEmpty()) {
            concatenatingMediaSource.clear()
            val mediaSources: MediaSources =
                ShadhinMediaSources(context, playList, exoplayerCache, musicRepository)
            concatenatingMediaSource.addMediaSources(mediaSources.createSources())
            exoPlayer?.clearMediaItems()
            exoPlayer?.addMediaSource(concatenatingMediaSource)
            exoPlayer?.seekToDefaultPosition(defaultPosition)
            onPrepare(true)
        }
    }

    fun isLive(): Boolean {
        return exoPlayer?.currentMediaItem?.toMusic()?.isLive() == true
    }

    fun initPlayList(playlist: MusicPlayList) {
        playList.clear()
        playList.addAll(playlist.list)
        concatenatingMediaSource.clear()
        val mediaSources: MediaSources =
            ShadhinMediaSources(context, playList, exoplayerCache, musicRepository)
        concatenatingMediaSource.addMediaSources(mediaSources.createSources())
        exoPlayer?.clearMediaItems()

        exoPlayer?.addMediaSource(concatenatingMediaSource) //todo This line make memory leak when app is destroy
        exH {
            if (defaultPosition != -1) {
                exoPlayer?.seekToDefaultPosition(defaultPosition)
            }
        }
    }

    override fun onPrepare(playWhenReady: Boolean) {
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
        playListUpdateFunc?.invoke(defaultPosition)
    }

    fun addPlaylist(playlist: MusicPlayList) {
        val mediaSources: MediaSources =
            ShadhinMediaSources(context, playList, exoplayerCache, musicRepository)
        concatenatingMediaSource.addMediaSources(mediaSources.createSources())
        playList.addAll(playlist.list)
        //playlist.loadAllImage(context)
        exoPlayer?.currentMediaItemIndex?.let { playListUpdateFunc?.invoke(it) }
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        val index = playList.indexOfFirst { song -> song.mediaId == mediaId }
        exoPlayer?.seekTo(index, 0L)
        exoPlayer?.playWhenReady = true
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    fun seekToDefaultPositionAndPrepare() {
        exoPlayer?.seekToDefaultPosition()
        exoPlayer?.prepare()
    }

    fun seekTo(l: Long) {
        exoPlayer?.seekTo(l)
    }

    fun currentMusic(): Music? {
        return exoPlayer?.currentMediaItem?.toMusic()
    }

    fun playerProgress(): PlayerProgress? {
        return exoPlayer?.let { PlayerProgress.fromPlayer(it) }
    }

    fun musicAt(windowIndex: Int): Music? {
        return exoPlayer?.getMediaItemAt(windowIndex)?.toMusic()
    }

    fun sendError(
        isDataSourceError: Boolean,
        errorMessage: String?,
        errorCode: Int?,
        currentMusic: Music?
    ) {
        errorCallbackFunc?.invoke(isDataSourceError, errorMessage, errorCode, currentMusic)
    }
}