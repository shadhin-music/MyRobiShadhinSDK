package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall
import kotlinx.coroutines.*

class SubscriptionCheckRepositoryImpl(private val subscriptionApiService: SubscriptionApiService) :
    SubscriptionCheckRepository {
    private var isActive: Boolean = false
    private var activeCheckJob: Deferred<Plan?>? = null
    override suspend fun haveActiveSubscriptionPlan(): Boolean {
        if (activeCheckJob != null) {
            activeCheckJob?.await()
        } else {
            fetchSubscriptionPlan()
        }
        return isActive
    }

    override suspend fun fetchSubscriptionPlan(): Plan? {
        withContext(Dispatchers.IO) {
            activeCheckJob = async {
                parseSubscriptionPlan()
            }
        }
        return activeCheckJob?.await()
    }

    private suspend fun parseSubscriptionPlan(): SubscriptionPlan? {
        val plans = subscriptionApiService.fetchSubscriptionPlans()
        delay(1000*5)
        val plan = kotlin.runCatching {
            plans?.first { it.status == Status.SUBSCRIBED }
        }.getOrNull()
        isActive = plan != null
        return plan
    }
}