package com.shadhinmusiclibrary.library.player.connection

import com.shadhinmusiclibrary.library.player.data.model.Music
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.library.player.data.model.PlayerProgress
import java.util.*

internal typealias CurrentMusicCallbackFunc = (music: Music?) -> Unit

internal interface MusicServiceActions {
    val currentMusic: Music?
    val musicList: List<Music>?
    val musicIndex: Int
    val isPlaying: Boolean
    val isPaused: Boolean
    val isPrepare: Boolean
    val isBuffering: Boolean
    fun subscribe(playlist: MusicPlayList, isPlayWhenReady: Boolean, position: Int)

    fun unSubscribe()
    fun connect()
    fun disconnect()


    fun addToQueue(music: Music)
    fun addPlayList(playlist: MusicPlayList, responseFunc: ((size: Int?) -> Unit)?)
    fun addPlayList(playlist: MusicPlayList)
    fun musicPosition(mediaId: String): Int
    fun stop()
    fun isMediaDataAvailable(): Boolean
    fun skipToQueueItem(position: Int)
    fun fastForward()
    fun rewind()
    fun playbackSpeed(speed: Float)
    fun repeatTrack()
    fun shuffleToggle()
    fun shuffle(isShuffle: Boolean)
    fun togglePlayPause()
    fun pause()
    fun play()
    fun prepare()
    fun skipToPrevious()
    fun skipToNext()
    fun seekTo(progress: Long)
    fun sleepTimer(isStart: Boolean, timeMillis: Long)
    fun sleepTime(callback: (startTime: Date?, duration: Long) -> Unit)
    fun playerProgress(playerProgressCallbackFunc: (PlayerProgress) -> Unit)
    suspend fun playerProgress(): PlayerProgress
    fun receiveErrorMessage()
    fun reAssignAll()
}