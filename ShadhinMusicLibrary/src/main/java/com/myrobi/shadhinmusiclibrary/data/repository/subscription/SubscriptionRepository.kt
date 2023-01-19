package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan

internal interface SubscriptionRepository {

    suspend fun haveActiveSubscriptionPlan(reload:Boolean = false):Boolean
    suspend fun fetchSubscriptionPlan(reload:Boolean = false):Plan?
    suspend fun subscriptionRequest(paymentMethod: PaymentMethod){

    }
    suspend fun plans():List<Plan>?{
        return emptyList()
    }

}