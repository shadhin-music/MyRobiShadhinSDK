package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse

internal class SubscriptionRepositoryImpl(
    private val subscriptionCheckRepository: SubscriptionCheckRepository,
    private val paymentMethodRepositoryFactory: PaymentMethodRepositoryFactory
):SubscriptionRepository {

    override suspend fun haveActiveSubscriptionPlan(reload:Boolean): Boolean {
        return subscriptionCheckRepository.haveActiveSubscriptionPlan(reload)
    }

    override suspend fun fetchSubscriptionPlan(reload:Boolean): Plan? {
        return subscriptionCheckRepository.fetchSubscriptionPlan(reload)
    }

    override suspend fun subscriptionRequest(paymentMethod: PaymentMethod): SubscriptionResponse? {
        return paymentMethodRepositoryFactory
            .repository(paymentMethod)
            .subscriptionRequest()
    }

    override suspend fun plans(paymentMethod: PaymentMethod): List<Plan>? {
        return paymentMethodRepositoryFactory
            .repository(paymentMethod)
            .plans()
    }

    override suspend fun cancel(paymentMethod: PaymentMethod) {
        paymentMethodRepositoryFactory
            .repository(paymentMethod)
            .cancel()
    }

}