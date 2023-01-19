package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod

internal class PaymentMethodRepositoryFactory {
    fun repository(paymentMethod: PaymentMethod):PaymentMethodRepository{
        return when(paymentMethod){
            is PaymentMethod.Robi -> RobiPaymentMethodRepository(paymentMethod)

            else -> throw IllegalArgumentException("Payment method not found")
        }
    }
}