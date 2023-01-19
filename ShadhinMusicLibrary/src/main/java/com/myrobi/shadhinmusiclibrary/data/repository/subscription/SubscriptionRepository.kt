package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse

internal interface SubscriptionRepository {

    suspend fun haveActiveSubscriptionPlan(reload:Boolean = false):Boolean
    suspend fun fetchSubscriptionPlan(reload:Boolean = false):Plan?
    suspend fun subscriptionRequest(paymentMethod: PaymentMethod): SubscriptionResponse?
    suspend fun plans(paymentMethod: PaymentMethod):List<Plan>?

}