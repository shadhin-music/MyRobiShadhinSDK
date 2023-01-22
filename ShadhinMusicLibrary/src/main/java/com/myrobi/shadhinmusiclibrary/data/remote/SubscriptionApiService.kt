package com.myrobi.shadhinmusiclibrary.data.remote

import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SubscriptionApiService {
    @GET("SubscriptionDetails/RobiUserSubscriberStatus")
    suspend fun fetchSubscriptionPlans(
    ): List<SubscriptionPlan>?

    @GET("RobiDCB/CancelSubsRDCB")
   suspend fun cancelRobi(
        @Query("mid") mobileNo: String?,
        @Query("subscriptionID") subscriptionID: String?
    )
}