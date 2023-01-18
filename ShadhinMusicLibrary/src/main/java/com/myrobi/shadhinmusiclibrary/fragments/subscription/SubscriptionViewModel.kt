package com.myrobi.shadhinmusiclibrary.fragments.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository): ViewModel() {

    fun fetchSubscriptionPlan() = viewModelScope.launch {
        subscriptionRepository.fetchSubscriptionPlan()
    }
}