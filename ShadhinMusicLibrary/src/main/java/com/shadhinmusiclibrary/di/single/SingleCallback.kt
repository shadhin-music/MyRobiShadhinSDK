package com.shadhinmusiclibrary.di.single

import com.shadhinmusiclibrary.ShadhinSDKCallback
import com.shadhinmusiclibrary.library.player.data.rest.PlayerApiService
import retrofit2.Retrofit

class SingleCallback {
    companion object {
         var INSTANCE: ShadhinSDKCallback? = null

        fun destroy() {
            INSTANCE = null
        }
    }
}