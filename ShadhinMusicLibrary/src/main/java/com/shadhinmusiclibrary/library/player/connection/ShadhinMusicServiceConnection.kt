package com.shadhinmusiclibrary.library.player.connection

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shadhinmusiclibrary.library.player.Constants
import com.shadhinmusiclibrary.library.player.ShadhinMusicPlayer
import com.shadhinmusiclibrary.library.player.data.model.ErrorMessage
import com.shadhinmusiclibrary.library.player.data.model.Music
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.library.player.data.model.PlayerProgress
import com.shadhinmusiclibrary.library.player.utils.*
import com.shadhinmusiclibrary.utils.toDate
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.absoluteValue

internal typealias BundleCallbackFunc = (resultData: Bundle?) -> Unit

internal class ShadhinMusicServiceConnection(
    private val context: Context,
    /*private val downloadAccess: OfflineDownloadDaoAccess,
    private val cacheRepository: CacheRepository*/
) : MusicServiceController {

    private var mediaControllerCompat: MediaControllerCompat? = null
    var transportControls: MediaControllerCompat.TransportControls? = null
    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()
    private var playbackStateListeners: PlaybackStateListeners? = null
    private val subscriptionCallback: ShadhinMusicSubscriptionCallback by lazy { ShadhinMusicSubscriptionCallback() }
    private val mediaControllerCallback: MediaControllerCallback = MediaControllerCallback()

    private val _playbackState: MutableLiveData<PlaybackStateCompat> by lazy { MutableLiveData<PlaybackStateCompat>() }
    private val _currentPlayingSong: MutableLiveData<Music?> = MutableLiveData<Music?>(null)
    private val _musicListLiveData: MutableLiveData<MusicPlayList> by lazy { MutableLiveData<MusicPlayList>() }
    private val _repeatModeLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val _shuffleLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val _playerErrorLiveData: MutableLiveData<ErrorMessage> by lazy { MutableLiveData<ErrorMessage>() }

    override val currentMusicLiveData: LiveData<Music?> = _currentPlayingSong
    override val playerErrorLiveData: LiveData<ErrorMessage> = _playerErrorLiveData
    override val playListLiveData: LiveData<MusicPlayList> = _musicListLiveData
    override val playbackStateLiveData: LiveData<PlaybackStateCompat> = _playbackState
    override val repeatModeLiveData: LiveData<Int> = _repeatModeLiveData
    override val shuffleLiveData: LiveData<Int> = _shuffleLiveData

    override val musicIndexLiveData: LiveData<Int> =
        Transformations.map(currentMusicLiveData) { cMusic ->
            val musicList = _musicListLiveData.value?.list
            if (!musicList.isNullOrEmpty()) musicList.indexOfFirst { music -> music.mediaId == cMusic?.mediaId } else 0
        }

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            ShadhinMusicPlayer::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    )
    val controller: MusicServiceActions
        get() = this

    override val currentMusic: Music?
        get() = _currentPlayingSong.value
    override val musicList: List<Music>?
        get() = _musicListLiveData.value?.list
    override val musicIndex: Int
        get() = musicIndexLiveData.value ?: 0

    override val isPlaying: Boolean
        get() {
            return playbackStateLiveData.value != null && playbackStateLiveData.value?.isPlaying == true
        }
    override val isPaused: Boolean
        get() {
            return playbackStateLiveData.value != null && playbackStateLiveData.value?.isPaused == true
        }
    override val isPrepare: Boolean
        get() {
            return playbackStateLiveData.value != null && playbackStateLiveData.value?.isPrepare == true
        }
    override val isBuffering: Boolean
        get() {
            return playbackStateLiveData.value != null && playbackStateLiveData.value?.isBuffering == true
        }

    override fun subscribe(playlist: MusicPlayList, isPlayWhenReady: Boolean, position: Int) {
        preloadBitmapClear()
        preLoadBitmap(playlist, context)
        // playlist.decodePlayUrl()
        playListUpdate { isReady ->
            if (isReady) {
                val bundle = playlist.toBundle(Command.SUBSCRIBE.dataKey).apply {
                    putBoolean(Constants.PLAY_WHEN_READY_KEY, isPlayWhenReady)
                    putInt(Constants.DEFAULT_POSITION_KEY, position)
                }
                mediaBrowser.subscribe(Constants.ROOT_ID_PLAYLIST, bundle, subscriptionCallback)
            }
        }
        receiveErrorMessage()
    }

    override fun unSubscribe() {
        sendCommand(Command.UNSUBSCRIBE)
        mediaBrowser.unsubscribe(Constants.ROOT_ID_PLAYLIST, subscriptionCallback)
        //  connectionScope?.cancel()
    }

    override fun addPlayList(playlist: MusicPlayList, responseFunc: ((size: Int?) -> Unit)?) {
        //    connectionScope?.asyncCallback({margeWithLocalUrl(playlist)}){
        preLoadBitmap(playlist, context)
        val command = Command.ADD_PLAYLIST
        playlist.let {
            sendCommand(command.tag, it.toBundle(command)) {
                val size = it?.getInt(command.dataKey)
                responseFunc?.invoke(size)
            }
        }
    }

    override fun connect() {
        if (!mediaBrowser.isConnected) {
            kotlin.runCatching { mediaBrowser.connect() }
        }
    }

    override fun disconnect() {
        kotlin.runCatching { mediaControllerCompat?.unregisterCallback(mediaControllerCallback) }
        kotlin.runCatching { mediaBrowser.disconnect() }
    }

    override fun addToQueue(music: Music) = addPlayList(MusicPlayList(listOf(music)))

    override fun addPlayList(playlist: MusicPlayList) {
        addPlayList(playlist, null)
    }

    private fun playListUpdate(isReady: (Boolean) -> Unit) {
        sendCommand(Command.GET_PLAYLIST) {
            if (it != null) {
                val playlist = it.toMusicPlayList(Command.GET_PLAYLIST)
                _musicListLiveData.postValue(playlist)
            } else {
                isReady(true)
            }
        }
    }

    override fun receiveErrorMessage() {
        sendCommand(Command.ERROR_CALLBACK) {
            _playerErrorLiveData.postValue(ErrorMessage.fromBundle(it))
        }
    }

    override fun reAssignAll() {
        if (/*mediaControllerCompat?.playbackState?.isPlaying == true &&*/ _currentPlayingSong.value == null) {
            sendCommand(Command.RE_ASSIGN_CALLBACK) {
                _currentPlayingSong.value =
                    it?.getSerializable(Command.RE_ASSIGN_CALLBACK.dataKey) as? Music?
                _musicListLiveData.value =
                    it?.getSerializable(Command.RE_ASSIGN_CALLBACK.dataKey3) as? MusicPlayList
            }
            _playbackState.postValue(mediaControllerCompat?.playbackState)
        }
    }

    override fun musicPosition(mediaId: String): Int {
        return musicList?.indexOfFirst { music -> music.mediaId == mediaId } ?: -1
    }

    override fun stop() = sendCommand(Command.STOP_SERVICE)

    override fun isMediaDataAvailable(): Boolean =
        !musicList.isNullOrEmpty() && (musicIndex in 0 until (musicList?.size ?: 0))

    override fun skipToQueueItem(position: Int) {
        if (musicIndex != position) {
            mediaControllerCompat?.transportControls?.skipToQueueItem(position.toLong())
        }
    }

    override fun fastForward() {
        transportControls?.fastForward()
    }

    override fun rewind() {
        transportControls?.rewind()
    }

    override fun playbackSpeed(speed: Float) {
        val bundle = Bundle().apply { putFloat(Command.PLAYER_SPEED.tag, speed) }
        sendCommand(Command.PLAYER_SPEED, bundle)
    }

    override fun repeatTrack() {
        if (mediaControllerCompat != null) {
            when (mediaControllerCompat?.repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_ALL -> transportControls?.setRepeatMode(
                    PlaybackStateCompat.REPEAT_MODE_NONE
                )
                PlaybackStateCompat.REPEAT_MODE_ONE -> transportControls?.setRepeatMode(
                    PlaybackStateCompat.REPEAT_MODE_ALL
                )
                else -> transportControls?.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
            }
        }
    }

    override fun shuffleToggle() {
        if (mediaControllerCompat != null) {
            val shuffle: Int? = mediaControllerCompat?.shuffleMode
            if (shuffle == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
                transportControls?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
            } else {
                transportControls?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
            }
        }
    }

    override fun shuffle(isShuffle: Boolean) {
        if (mediaControllerCompat != null) {
            if (isShuffle) {
                transportControls?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
            } else {
                transportControls?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
            }
        }
    }

    override fun togglePlayPause() {
        if (isPrepare) {
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        }
    }

    override fun pause() {
        transportControls?.pause()
    }

    override fun play() {
        transportControls?.play()
    }

    override fun prepare() {
        transportControls?.prepare()
    }

    override fun skipToPrevious() {
        transportControls?.skipToPrevious()
    }

    override fun skipToNext() {
        transportControls?.skipToNext()
    }

    override fun seekTo(progress: Long) {
        transportControls?.seekTo(progress)
    }

    override fun sleepTimer(isStart: Boolean, timeMillis: Long) {
        val bundle = Bundle().apply {
            putLong(Command.SLEEP_TIMER.tag, timeMillis)
            putBoolean(Command.SLEEP_TIMER.tag2, isStart)
        }
        sendCommand(Command.SLEEP_TIMER, bundle)
    }

    override fun playbackState(playbackStateListeners: PlaybackStateListeners) {
        this.playbackStateListeners = playbackStateListeners
    }

    override fun sleepTime(callback: (startTime: Date?, duration: Long) -> Unit) {
        sendCommand(Command.GET_SLEEP_TIME) {
            val duration = it?.getLong(Command.GET_SLEEP_TIME.dataKey)
            val startTime = it?.getString(Command.GET_SLEEP_TIME.dataKey2)
            callback.invoke(startTime?.toDate(), duration ?: -1)
        }
    }

    override suspend fun playerProgress(): PlayerProgress =
        suspendCoroutine { coro ->
            playerProgress {
                coro.resume(it)
            }
        }

    override fun playerProgress(playerProgressCallbackFunc: (PlayerProgress) -> Unit) {
        reAssignAll()
//        Log.e("SMSC", "playerProgress: " + _musicListLiveData.value?.list?.size)

        sendCommand(Command.MUSIC_PROGRESS_REQUEST) {
            val playerProgress = it?.toPlayerProgress(Command.MUSIC_PROGRESS_REQUEST.dataKey)
            playerProgress?.let { it1 -> playerProgressCallbackFunc(it1) }
        }
    }

    private fun sendCommand(
        command: Command,
        inputBundle: Bundle? = null,
        resultCallbackFunc: BundleCallbackFunc? = null
    ) = sendCommand(command.tag, inputBundle, resultCallbackFunc)

    private fun sendCommand(
        command: String,
        inputBundle: Bundle? = null,
        resultCallbackFunc: BundleCallbackFunc? = null
    ) {
        mediaControllerCompat?.sendCommand(
            command,
            inputBundle,
            MusicResultReceiver(command.hashCode().absoluteValue, resultCallbackFunc)
        )
    }

    inner class ShadhinMusicSubscriptionCallback : MediaBrowserCompat.SubscriptionCallback()

    inner class MusicResultReceiver(
        private val requestCode: Int,
        private val resultCallbackFunc: BundleCallbackFunc?
    ) : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == requestCode) {
                resultCallbackFunc?.invoke(resultData)
            }
        }
    }

    inner class MediaBrowserConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            mediaControllerCompat = MediaControllerCompat(context, mediaBrowser.sessionToken)
            transportControls = mediaControllerCompat?.transportControls
            mediaControllerCompat?.registerCallback(mediaControllerCallback)
        }
    }

    inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playbackState.postValue(state)
            playbackStateListeners?.stateChange(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            val music = metadata?.toMusic()
            _currentPlayingSong.value = music
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            super.onShuffleModeChanged(shuffleMode)
            _shuffleLiveData.postValue(shuffleMode)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            _repeatModeLiveData.postValue(repeatMode)
        }
    }

    enum class Command(val tag: String, isNeedCallBack: Boolean) {
        ADD_PLAYLIST("add_play_list", true),
        CHANGE_MUSIC("set_play_position", false),
        SUBSCRIBE("subscribe", false),
        STOP_SERVICE("stop_player_service", false),
        GET_PLAYLIST("play_list_update", true),
        PLAYER_SPEED("player_speed", false),
        MUSIC_PROGRESS_REQUEST("music_position", true),
        SLEEP_TIMER("sleep_timer", false),
        UNSUBSCRIBE("unsubscribe_player", false),
        GET_SLEEP_TIME("sleep_time_get", true),
        ERROR_CALLBACK("music_position", true),
        RE_ASSIGN_CALLBACK("re_assign_callback", true);

        val resultCode: Int
            get() = tag.hashCode().absoluteValue
        val dataKey: String
            get() = tag
        val dataKey2: String
            get() = tag2
        val tag2: String
            get() = "${tag}a"
        val dataKey3: String
            get() = tag3
        val tag3: String
            get() = "${tag}b"
    }
}