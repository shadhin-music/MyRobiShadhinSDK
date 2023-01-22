package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse

internal interface PaymentMethodRepository {
    suspend fun subscriptionRequest(): SubscriptionResponse?
    suspend fun plans():List<Plan>?
    suspend fun cancel()
}