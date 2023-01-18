package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan

interface SubscriptionCheckRepository {
    suspend fun haveActiveSubscriptionPlan():Boolean
    suspend fun fetchSubscriptionPlan(): Plan?
}