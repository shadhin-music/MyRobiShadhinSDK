package com.myrobi.shadhinmusiclibrary.library.player.data.rest.user_history

import com.myrobi.shadhinmusiclibrary.library.player.data.rest.PlayerApiService
import com.myrobi.shadhinmusiclibrary.utils.postContentType
import com.myrobi.shadhinmusiclibrary.utils.preContentType


internal class UserHistoryRepositoryImpl(private val playerApiService: PlayerApiService) :
    UserHistoryRepository {

    override suspend fun postHistory(
        isPD: Boolean,
        isVideo: Boolean,
        conId: String,
        type: String,
        playCount: String,
        time: Int,
        sTime: String,
        eTime: String,
        userPlayListId: String?,
        isLive:Boolean
    ) {


        val jsonParams = HashMap<String?, Any?>()
        jsonParams["ContentId"] = conId
        jsonParams["PlayCount"] = playCount
        jsonParams["TimeCountSecond"] = time
        jsonParams["PlayIn"] = sTime
        jsonParams["PlayOut"] = eTime
        if (userPlayListId != null) {
            jsonParams["UserPlayListId"] = userPlayListId
        }

        if(!isLive) {
            if (isPD || isVideo) {
                jsonParams["Type"] = type.trim().uppercase().preContentType()
                jsonParams["ContentType"] = type.trim().uppercase().postContentType()
                try {
                    playerApiService.trackPodcastPlaying(jsonParams)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                jsonParams["Type"] = type
                try {
                    playerApiService.trackSongPlaying(jsonParams)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }else{
            jsonParams["Type"] = type.trim().uppercase().preContentType()
            jsonParams["ContentType"] = type.trim().uppercase()
            val delay = (1..5).random()*1000L
            kotlinx.coroutines.delay(delay)
            playerApiService.trackPodcastLivePlaying(jsonParams)
        }
    }
}