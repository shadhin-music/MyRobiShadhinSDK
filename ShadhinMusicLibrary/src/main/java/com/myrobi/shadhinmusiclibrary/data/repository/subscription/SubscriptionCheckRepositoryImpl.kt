package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import kotlinx.coroutines.*

class SubscriptionCheckRepositoryImpl(private val subscriptionApiService: SubscriptionApiService) :
    SubscriptionCheckRepository {

    private var subscriptionJob: Deferred<Plan?>? = null

    override suspend fun haveActiveSubscriptionPlan(reload:Boolean): Boolean {
        val plan = fetchSubscriptionPlan(reload)
        return plan !=null
    }

    override suspend fun fetchSubscriptionPlan(reload:Boolean): Plan? {

        suspend fun reload(){
            subscriptionJob?.cancelAndJoin()
            withContext(Dispatchers.IO) {
                subscriptionJob = async {
                    parseSubscriptionPlan()
                }
            }
        }
        if(reload || subscriptionJob == null){
            reload()
        }
        return subscriptionJob?.await()
    }

    private suspend fun parseSubscriptionPlan(): SubscriptionPlan? {
        val plans = subscriptionApiService.fetchSubscriptionPlans()
        return kotlin.runCatching {
            plans?.first { it.status == Status.SUBSCRIBED }
        }.getOrNull()
    }
}