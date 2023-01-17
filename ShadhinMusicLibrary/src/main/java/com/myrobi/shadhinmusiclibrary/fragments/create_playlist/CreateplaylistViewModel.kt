package com.myrobi.shadhinmusiclibrary.fragments.create_playlist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.myrobi.shadhinmusiclibrary.data.repository.CreatePlaylistRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch
import org.json.JSONObject


internal class CreateplaylistViewModel (private val createPlaylistRepository: CreatePlaylistRepository): ViewModel() {

    private val _createPlaylist: MutableLiveData<CreatePlaylistResponseModel> = MutableLiveData()
    val createPlaylist: LiveData<CreatePlaylistResponseModel> = _createPlaylist

    private val _songsAddedToPlaylist: MutableLiveData<CreatePlaylistResponseModel> = MutableLiveData()
    val songsAddedToPlaylist: LiveData<CreatePlaylistResponseModel> = _songsAddedToPlaylist

    private val _getUserPlaylist: MutableLiveData<UserPlayListModel> = MutableLiveData()
    val getUserPlaylist: LiveData<UserPlayListModel> = _getUserPlaylist

    private val _getUserSongsPlaylist: MutableLiveData<UserSongsPlaylistModel> = MutableLiveData()
    val getUserSongsPlaylist: LiveData<UserSongsPlaylistModel> =  _getUserSongsPlaylist

    private val _deleteUserSongFromPlaylist: MutableLiveData<CreatePlaylistResponseModel> = MutableLiveData()
    val deleteUserSongFromPlaylist: LiveData<CreatePlaylistResponseModel> =  _deleteUserSongFromPlaylist

    private val _deletePlaylist: MutableLiveData<CreatePlaylistResponseModel> = MutableLiveData()
    val deletePlaylist: LiveData<CreatePlaylistResponseModel> =  _deletePlaylist

    fun createPlaylist(name: String) = viewModelScope.launch {
        val response = createPlaylistRepository.createPlaylist(name)
        if(response.status == Status.SUCCESS && response.data !=null){
            _createPlaylist.postValue(response.data)
        }

    }

    fun getuserPlaylist() = viewModelScope.launch {
        val response = createPlaylistRepository.getUserPlaylist()
        _getUserPlaylist.postValue(response.data)
    }
    fun songsAddedToPlaylist(playlistId: String,contentId:String) = viewModelScope.launch {
        val response = createPlaylistRepository.songsAddedtoPlaylist(playlistId, contentId)
        _songsAddedToPlaylist.postValue(response.data)
    }

    fun getuserSongsInPlaylist(id:String) = viewModelScope.launch {
        val response = createPlaylistRepository.getUserSongsInPlaylist(id)
        _getUserSongsPlaylist.postValue(response.data)
    }
    fun deleteuserSongfromPlaylist(playlistId: String,contentId:String) = viewModelScope.launch {
        val response = createPlaylistRepository.songDeletedfromPlaylist(playlistId,contentId)
        _deleteUserSongFromPlaylist.postValue(response.data)
    }

    fun deleteuserPlaylist(playlistId: String) = viewModelScope.launch {
        val response = createPlaylistRepository.deletePlaylist(playlistId)
        _deletePlaylist.postValue(response.data)
    }
}