package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import kotlinx.coroutines.*
import okhttp3.internal.wait

class SubscriptionRepositoryImpl(private val subscriptionCheckRepository: SubscriptionCheckRepository):SubscriptionRepository {

    override suspend fun haveActiveSubscriptionPlan(reload:Boolean): Boolean {
        return subscriptionCheckRepository.haveActiveSubscriptionPlan(reload)
    }

    override suspend fun fetchSubscriptionPlan(reload:Boolean): Plan? {
        return subscriptionCheckRepository.fetchSubscriptionPlan(reload)
    }
    private fun load(){

    }
}