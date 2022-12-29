package com.shadhinmusiclibrary.di

import android.app.Service

internal interface ServiceEntryPoint {
    val injector: Module
        get() = ShadhinApp.module((this as Service).applicationContext)
}