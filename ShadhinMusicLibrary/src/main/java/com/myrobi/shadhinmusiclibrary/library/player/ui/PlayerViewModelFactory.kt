package com.myrobi.shadhinmusiclibrary.library.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.UserSessionRepository
import com.myrobi.shadhinmusiclibrary.library.player.connection.MusicServiceController

internal class PlayerViewModelFactory(
    private val shadhinMusicServiceConnection: MusicServiceController,
    private val userSessionRepository: UserSessionRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(shadhinMusicServiceConnection,userSessionRepository) as T
    }
}