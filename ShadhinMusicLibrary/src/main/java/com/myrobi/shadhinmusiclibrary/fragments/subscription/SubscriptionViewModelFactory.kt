package com.myrobi.shadhinmusiclibrary.fragments.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository

class SubscriptionViewModelFactory(private val subscriptionRepository: SubscriptionRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscriptionViewModel(subscriptionRepository) as T
    }
}