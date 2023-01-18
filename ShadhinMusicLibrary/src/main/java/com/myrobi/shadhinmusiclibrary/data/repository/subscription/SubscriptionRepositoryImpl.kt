package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan

class SubscriptionRepositoryImpl(private val subscriptionCheckRepository: SubscriptionCheckRepository):SubscriptionRepository {
    override suspend fun haveActiveSubscriptionPlan(): Boolean {
        return subscriptionCheckRepository.haveActiveSubscriptionPlan()
    }

    override suspend fun fetchSubscriptionPlan(): Plan? {
        return subscriptionCheckRepository.fetchSubscriptionPlan()
    }
}