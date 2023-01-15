package com.myrobi.shadhinmusiclibrary.library.player.audio_focus

import android.content.Context
import android.media.AudioManager

class PreOreoAudioFocusManager:AudioFocusManager{
    private var audioManager:AudioManager?=null
    private var listener:AudioManager.OnAudioFocusChangeListener?=null
    override fun initialize(context: Context, listener: AudioManager.OnAudioFocusChangeListener) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        this.listener = listener
    }

    override fun requestAudioFocus(): Int? {
       return audioManager?.requestAudioFocus(listener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)
    }
}