package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.model.PlaylistBody
import com.shadhinmusiclibrary.data.model.SongsAddedtoPlaylistBody
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class CreatePlaylistRepository(private val apiService: ApiService) {


    suspend fun createPlaylist(name: String) = safeApiCall {
        apiService.createPlaylist(PlaylistBody(name))
    }

    suspend fun getUserPlaylist() = safeApiCall {
        apiService.getUserPlaylist()
    }

    suspend fun songsAddedtoPlaylist(playlistId: String, contentId: String) = safeApiCall {
        apiService.songsAddedtoPlaylist(SongsAddedtoPlaylistBody(playlistId, contentId))
    }

    suspend fun getUserSongsInPlaylist(id: String) = safeApiCall {
        apiService.getUserSongsInPlaylist(id)
    }

    suspend fun songDeletedfromPlaylist(playlistId: String, contentId: String) = safeApiCall {
        apiService.songDeletedfromPlaylist(SongsAddedtoPlaylistBody(playlistId, contentId))
    }

    suspend fun deletePlaylist(playlistId: String) = safeApiCall {
        apiService.deletePlaylist(playlistId)
    }
}
