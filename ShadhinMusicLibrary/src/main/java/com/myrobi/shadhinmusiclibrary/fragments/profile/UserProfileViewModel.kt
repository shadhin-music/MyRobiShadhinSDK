package com.myrobi.shadhinmusiclibrary.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataResponseModel
import com.myrobi.shadhinmusiclibrary.data.model.profile.ProfileupdateModel
import com.myrobi.shadhinmusiclibrary.data.model.profile.UserProfileResponseModel
import com.myrobi.shadhinmusiclibrary.data.repository.UserProfileRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import kotlinx.coroutines.launch
import java.io.File

internal class UserProfileViewModel (private val userProfileRepository: UserProfileRepository): ViewModel() {
    private val _updateProfile: MutableLiveData<ApiResponse<UserProfileResponseModel>> = MutableLiveData()
    val updateProfile: MutableLiveData<ApiResponse<UserProfileResponseModel>> = _updateProfile
    private val _getProfile: MutableLiveData<ApiResponse<UserProfileResponseModel>> = MutableLiveData()
    val getProfile:MutableLiveData<ApiResponse<UserProfileResponseModel>> = _getProfile
    var profileImageFile: File?=null

    fun getUserInfo() = viewModelScope.launch {
        val response = userProfileRepository.getUserInfo()
        _getProfile.postValue(response)
    }
    fun updateProfile(name: String,birthday:String,gender:String) = viewModelScope.launch {
        val response = userProfileRepository.updateUserProfile(name,birthday,gender,profileImageFile)
       // userProfileRepository.getUserInfo()
        _updateProfile.postValue(response)
    }

}