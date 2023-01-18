package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall
import kotlinx.coroutines.*

class SubscriptionCheckRepositoryImpl(private val subscriptionApiService: SubscriptionApiService):SubscriptionCheckRepository {

    override suspend fun haveActiveSubscriptionPlan(): Boolean {
         return false
    }

    override suspend fun fetchSubscriptionPlan(): Plan? {
        return subscriptionApiService.fetchSubscriptionPlans()?.first { it.status == Status.SUBSCRIBED }
    }
}