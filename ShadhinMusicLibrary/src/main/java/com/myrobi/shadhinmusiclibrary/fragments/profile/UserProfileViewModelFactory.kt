package com.myrobi.shadhinmusiclibrary.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.FavContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.UserProfileRepository
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel

internal class UserProfileViewModelFactory (private val userProfileRepository: UserProfileRepository):
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserProfileViewModel(userProfileRepository) as T
        }
}
