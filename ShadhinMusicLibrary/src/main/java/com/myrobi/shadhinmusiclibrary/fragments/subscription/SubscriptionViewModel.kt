package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository
import com.myrobi.shadhinmusiclibrary.utils.toApiError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository): ViewModel() {

   private val coroutineContext = Dispatchers.IO+CoroutineExceptionHandler { _, exception ->
       Log.i("SubscriptionViewModel", "${exception.toApiError().toString()}")
    }
    fun fetchSubscriptionPlan() = viewModelScope.launch(coroutineContext) {
        subscriptionRepository.fetchSubscriptionPlan()
    }
}