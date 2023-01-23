package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan

internal interface SubscriptionCheckRepository {
    suspend fun haveActiveSubscriptionPlan(reload:Boolean = false):Boolean
    suspend fun fetchSubscriptionPlan(reload:Boolean = false): Plan?
}