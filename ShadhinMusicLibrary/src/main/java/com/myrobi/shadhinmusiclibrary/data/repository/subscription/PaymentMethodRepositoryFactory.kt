package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService

internal class PaymentMethodRepositoryFactory(private val subscriptionApiService: SubscriptionApiService) {
    fun repository(paymentMethod: PaymentMethod):PaymentMethodRepository{
        return when(paymentMethod){
            is PaymentMethod.Robi -> RobiPaymentMethodRepository(paymentMethod,subscriptionApiService)

            else -> throw IllegalArgumentException("Payment method not found")
        }
    }
}