package com.shadhinmusiclibrary.library.player.data.rest.user_history

internal interface UserHistoryRepository {
    suspend fun postHistory(
        isPD: Boolean,
        isVideo:Boolean,
        conId: String,
        type: String,
        playCount: String,
        time: Int,
        sTime: String,
        eTime: String,
        userPlayListId: String?,
        isLive:Boolean = false
    )
}