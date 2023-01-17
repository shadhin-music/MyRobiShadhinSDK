package com.myrobi.shadhinmusiclibrary.library.player.data.model

import android.os.Bundle
import androidx.annotation.Keep

@Keep
internal data class ErrorMessage(val isDataSourceError:Boolean?, val message:String?, val errorCode:Int?=null,val  currentMusic: Music?=null){
    fun toBundle(): Bundle {
        val bundle = Bundle().apply {
            putBoolean("isDataSourceError",isDataSourceError?:false)
            putString("errorMessage",message)
            putInt("errorCode",errorCode?:0)
            putSerializable("currentMusic",currentMusic)
        }
        return bundle
    }
    companion object{
        fun fromBundle(bundle:Bundle?): ErrorMessage {
            return ErrorMessage(
                bundle?.getBoolean("isDataSourceError"),
                bundle?.getString("errorMessage"),
                bundle?.getInt("errorCode"),
                bundle?.getSerializable("currentMusic") as? Music
            )
        }
    }
}