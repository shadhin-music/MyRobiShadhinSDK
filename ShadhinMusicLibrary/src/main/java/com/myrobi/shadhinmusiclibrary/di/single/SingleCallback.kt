package com.myrobi.shadhinmusiclibrary.di.single


import com.myrobi.shadhinmusiclibrary.ShadhinSDKCallback
import retrofit2.Retrofit

class SingleCallback {
    companion object {
         var INSTANCE: ShadhinSDKCallback? = null

        fun destroy() {
            INSTANCE = null
        }
    }
}