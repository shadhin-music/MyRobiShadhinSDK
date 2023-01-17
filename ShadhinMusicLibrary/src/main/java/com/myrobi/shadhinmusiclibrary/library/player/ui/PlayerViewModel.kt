package com.myrobi.shadhinmusiclibrary.library.player.ui

import androidx.lifecycle.*
import com.myrobi.shadhinmusiclibrary.data.repository.UserSessionRepository
import com.myrobi.shadhinmusiclibrary.library.player.connection.MusicServiceController
import com.myrobi.shadhinmusiclibrary.library.player.data.model.PlayerProgress
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class PlayerViewModel(
    private val musicServiceController: MusicServiceController,
    private val userSessionRepository: UserSessionRepository
) :
    ViewModel(),
    MusicServiceController by musicServiceController {

    private val _playerProgress: MutableLiveData<PlayerProgress> = MutableLiveData()
    val playerProgress: LiveData<PlayerProgress> = _playerProgress



    init {
        connect()
    }

    //DO NOT Call this function multiple times
    fun startObservePlayerProgress(viewLifecycleOwner: LifecycleOwner) {
        fun update() = viewModelScope.launch {
            while (true) {
                delay(500)
                _playerProgress.postValue(musicServiceController.playerProgress())
            }
        }

        var updateJob: Job? = update()
        playbackState {
            if (it?.isPlaying == true) {
                updateJob?.cancel()
                updateJob = update()
            } else {
                updateJob?.cancel()
            }
        }
    }
    fun startUserSession()  = viewModelScope.launch{
        userSessionRepository.start()
    }
    fun endUserSession() = viewModelScope.launch(NonCancellable){
        userSessionRepository.end()
    }
}