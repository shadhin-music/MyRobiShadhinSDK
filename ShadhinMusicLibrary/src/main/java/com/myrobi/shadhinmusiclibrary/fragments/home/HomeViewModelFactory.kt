package com.myrobi.shadhinmusiclibrary.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.HomeContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository

internal class HomeViewModelFactory(
    private val homeContentRepository: HomeContentRepository,
    private val subscriptionRepository: SubscriptionRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(homeContentRepository,subscriptionRepository) as T
    }
}