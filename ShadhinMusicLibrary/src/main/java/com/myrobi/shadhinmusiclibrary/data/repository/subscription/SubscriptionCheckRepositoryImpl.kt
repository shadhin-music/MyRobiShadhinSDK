package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.BuildConfig
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionPlanStaticData.subscriptionPlanMap
import kotlinx.coroutines.*
import okhttp3.internal.wait

internal class SubscriptionCheckRepositoryImpl(private val subscriptionApiService: SubscriptionApiService) :
    SubscriptionCheckRepository {

    private var subscriptionJob: Deferred<Plan?>? = null

    override suspend fun haveActiveSubscriptionPlan(reload:Boolean): Boolean {
        val plan = fetchSubscriptionPlan(reload)
        return plan !=null
    }

    override suspend fun fetchSubscriptionPlan(reload:Boolean): Plan? {

        suspend fun reload(): Plan? {
            subscriptionJob?.cancelAndJoin()
            withContext(Dispatchers.IO) {
                subscriptionJob = async {
                    parseSubscriptionPlan()
                }
            }
            return subscriptionJob?.await()
        }

        return if(reload || subscriptionJob == null){
            reload()
        }else{
            runCatching { subscriptionJob?.await() }
                .getOrNull()
        }

    }


    private suspend fun parseSubscriptionPlan(): SubscriptionPlan? {
        val plans = subscriptionApiService.fetchSubscriptionPlans()
        val plan:SubscriptionPlan? = kotlin.runCatching {
            plans?.first { it.status == Status.SUBSCRIBED }
        }.getOrNull()

        val localPlanInfo = if(BuildConfig.DEBUG){
            subscriptionPlanMap["2250"]
        }else{
            subscriptionPlanMap[plan?.serviceId]
        }
        return  plan?.copy(
            type = localPlanInfo?.type,
            amount =  localPlanInfo?.amount,
            extraVatText = localPlanInfo?.extraVatText,
            isAutoRenewal = localPlanInfo?.isAutoRenewal
        )
    }
}