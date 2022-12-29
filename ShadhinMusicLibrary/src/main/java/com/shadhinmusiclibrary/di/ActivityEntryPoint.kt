package com.shadhinmusiclibrary.di

import android.app.Activity

internal interface ActivityEntryPoint {
    val injector: Module
        get() = ShadhinApp.module((this as Activity).applicationContext)
}