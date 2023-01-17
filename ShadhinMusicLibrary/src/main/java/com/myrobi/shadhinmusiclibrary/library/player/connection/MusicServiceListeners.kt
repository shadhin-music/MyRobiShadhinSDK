package com.myrobi.shadhinmusiclibrary.library.player.connection

import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import com.myrobi.shadhinmusiclibrary.library.player.data.model.ErrorMessage
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.library.player.data.model.MusicPlayList

internal interface MusicServiceListeners {
    val currentMusicLiveData: LiveData<Music?>
    val playerErrorLiveData:LiveData<ErrorMessage>
    val playListLiveData: LiveData<MusicPlayList>
    val playbackStateLiveData: LiveData<PlaybackStateCompat>
    val musicIndexLiveData:LiveData<Int>
    val repeatModeLiveData:LiveData<Int>
    val shuffleLiveData:LiveData<Int>
    fun playbackState(playbackStateListeners:PlaybackStateListeners)
}