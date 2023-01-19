package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils.BASE_URL_API_shadhinmusic

internal class RobiPaymentMethodRepository(private val paymentMethod: PaymentMethod.Robi):PaymentMethodRepository {
    override suspend fun subscriptionRequest(): SubscriptionResponse? {
        val robiUrl =  "${BASE_URL_API_shadhinmusic}RobiDCB/ReqSubsRDCB?mid=%s&subscriptionID=%s&chargetype=SMS&callbackurl=https://shadhinmusic.com/"
        val url = String.format(
            robiUrl,
            paymentMethod.plan?.msisdn,
            paymentMethod.plan?.serviceId
        )
        return SubscriptionResponse.Robi(url)
    }

    override suspend fun plans(): List<Plan>? {
        return SubscriptionConfig.robiPlans.values.toList()
    }

}