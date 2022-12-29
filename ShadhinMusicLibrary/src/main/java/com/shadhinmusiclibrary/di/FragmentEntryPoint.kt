package com.shadhinmusiclibrary.di

import androidx.fragment.app.Fragment

internal interface FragmentEntryPoint {
    val injector: Module
        get() = ShadhinApp.module((this as Fragment).requireContext().applicationContext)
}