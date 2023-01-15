package com.myrobi.shadhinmusiclibrary.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class UserSessionBody(

    @SerializedName("sessionStart")
    var sessionStart: String? = null,
    @SerializedName("sessionEnd")
    var sessionEnd: String? = null
){

    companion object{
        fun create(startTime:Date , endTime:Date): UserSessionBody {
           return UserSessionBody(sessionStart = format(startTime), sessionEnd = format(endTime) )
        }
        private fun format(time:Date): String? {
           return kotlin.runCatching {SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(time)}.getOrNull()
        }
    }
}
