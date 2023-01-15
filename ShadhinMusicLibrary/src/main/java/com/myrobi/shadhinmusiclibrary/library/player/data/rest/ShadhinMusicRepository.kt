package com.myrobi.shadhinmusiclibrary.library.player.data.rest

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.repository.AuthRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


import com.myrobi.shadhinmusiclibrary.library.player.singleton.DataSourceInfo

import kotlinx.coroutines.runBlocking

internal class ShadhinMusicRepository(private val playerApiService: PlayerApiService) :
    MusicRepository {

    override fun fetchURL(music: Music): String = runBlocking {

        val response = safeApiCall {
            playerApiService.fetchContentUrl(
                ptype = if (music.isPodCast()) "PD" else null,
                type = music.podcastSubType(),
                ttype = music.podcastTrackType(),
                name = if (!music.filePath().isNullOrEmpty()) music.filePath() else null
            )
        }

        val url = if (response.status == Status.SUCCESS && response.data?.data != null) {
            response.data.data
        } else {
            DataSourceInfo.isDataSourceError = true
            DataSourceInfo.dataSourceErrorCode = response.errorCode
            DataSourceInfo.dataSourceErrorMessage =
                response.data?.message ?: response.message ?: "Something wrong"
            null
        }
        return@runBlocking url.toString()
    }

    override fun refreshStreamingStatus() {
    }

    override fun fetchDownloadedURL(name: String): String = runBlocking {
        val response = safeApiCall {
            AuthRepository.appToken?.let {
                playerApiService.fetchDownloadContentUrl(
                    name = name
                )
            }
        }
        val url = if (response.status == Status.SUCCESS && response.data?.data != null) {
            response.data.data
        } else {
            DataSourceInfo.isDataSourceError = true
            DataSourceInfo.dataSourceErrorCode = response.errorCode
            DataSourceInfo.dataSourceErrorMessage =
                response.data?.message ?: response.message ?: "Something wrong"
            null
        }
        return@runBlocking url.toString()
    }
}