package com.myrobi.shadhinmusiclibrary

import androidx.annotation.Keep

@Keep
interface ShadhinSDKCallback {
    fun tokenStatus(isTokenValid: Boolean, error: String)
    fun onShare(rc: String)

}