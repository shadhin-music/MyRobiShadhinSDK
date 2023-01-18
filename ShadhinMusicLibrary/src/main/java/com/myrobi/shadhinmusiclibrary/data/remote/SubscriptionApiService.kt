package com.myrobi.shadhinmusiclibrary.data.remote

import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import retrofit2.http.GET

interface SubscriptionApiService {
    @GET("subscriptiondetails/get")
    suspend fun fetchSubscriptionPlans(
    ): List<SubscriptionPlan>?
}