package com.myrobi.shadhinmusiclibrary.library.player.audio_focus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class PostOreoAudioFocusManager:AudioFocusManager {
    private var audioManager:AudioManager?=null
    private lateinit var focusRequest: AudioFocusRequest

    override fun initialize(context: Context, listener: AudioManager.OnAudioFocusChangeListener) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(listener)
            .build()
    }

    override fun requestAudioFocus():Int? {
        return audioManager?.requestAudioFocus(focusRequest)
    }
}