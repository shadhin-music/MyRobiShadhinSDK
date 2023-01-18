package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository
import com.myrobi.shadhinmusiclibrary.utils.toApiError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

const val TAG = "SubscriptionViewModel"
class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository): ViewModel() {

   private val coroutineContext = Dispatchers.IO+CoroutineExceptionHandler { _, exception ->
       Log.i("SubscriptionViewModel", "${exception.toApiError().toString()}")
    }
    fun haveActiveSubscriptionPlan() = viewModelScope.launch(coroutineContext) {
        measureTimeMillis {
            val a = subscriptionRepository.haveActiveSubscriptionPlan()
            Log.i(TAG, "haveActiveSubscriptionPlan: $a")
        }.also {
            Log.i(TAG, "haveActiveSubscriptionPlan: $it ms")
        }
    }
    fun fetchSubscriptionPlan() = viewModelScope.launch(coroutineContext) {
        Log.i(TAG, "fetchSubscriptionPlan: start")
        measureTimeMillis {
            val plan = subscriptionRepository.fetchSubscriptionPlan()
        }.also {
            Log.i(TAG, "fetchSubscriptionPlan: $it ms")
        }


    }
}