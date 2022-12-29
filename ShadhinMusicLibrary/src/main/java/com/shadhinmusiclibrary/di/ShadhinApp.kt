package com.shadhinmusiclibrary.di

import android.content.Context
import com.shadhinmusiclibrary.library.player.utils.createMusicNotificationChannel

internal object ShadhinApp {
    private var _module: Module? = null

    fun module(context: Context): Module {
        createMusicNotificationChannel(context)
        return _module ?: synchronized(this) { _module ?: Module(context).also { _module = it } }
    }

    fun onDestroy() {
        _module = null
    }
}