package com.shadhinmusiclibrary.library.player.data.rest

import com.shadhinmusiclibrary.library.player.data.model.ContentUrlResponse
import com.shadhinmusiclibrary.library.player.data.model.SongTrackingModel
import retrofit2.http.*

internal interface PlayerApiService {

    @GET("clientstreaming/getpth")
    suspend fun fetchContentUrl(
        @Query("ptype") ptype: String?,
        @Query("type") type: String?,
        @Query("ttype") ttype: String?,
        @Query("name") name: String?

    ): ContentUrlResponse

    @GET("clientstreaming/getdwnpth")
    suspend fun fetchDownloadContentUrl(
        @Query("name") name: String?
    ): ContentUrlResponse

    @POST("ClientPodcast/PodcastUserHistoryV3")
    suspend fun trackPodcastPlaying(
        @Body body: HashMap<String?, Any?>?
    ): SongTrackingModel?

    @POST("ClientActivity/Livepodcasthistory")
    suspend fun trackPodcastLivePlaying(
        @Body body: HashMap<String?, Any?>?
    ): SongTrackingModel?

    @POST("ClientActivity/UserHistory")
    suspend fun trackSongPlaying(
        @Body body: HashMap<String?, Any?>?
    ): SongTrackingModel
}