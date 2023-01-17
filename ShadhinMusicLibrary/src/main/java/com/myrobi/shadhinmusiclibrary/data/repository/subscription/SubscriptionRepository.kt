package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.Subscription

interface SubscriptionRepository {

    suspend fun haveSubscription():Boolean
    suspend fun fetchSubscription():Subscription
    suspend fun subscriptionRequest()
    suspend fun plans()

}