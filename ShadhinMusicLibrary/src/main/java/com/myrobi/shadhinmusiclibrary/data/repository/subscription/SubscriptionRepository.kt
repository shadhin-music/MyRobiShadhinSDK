package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan

interface SubscriptionRepository {

    suspend fun haveActiveSubscriptionPlan(reload:Boolean = false):Boolean
    suspend fun fetchSubscriptionPlan(reload:Boolean = false):Plan?
    suspend fun subscriptionRequest(){

    }
    suspend fun plans():List<Plan>?{
        return emptyList()
    }

}