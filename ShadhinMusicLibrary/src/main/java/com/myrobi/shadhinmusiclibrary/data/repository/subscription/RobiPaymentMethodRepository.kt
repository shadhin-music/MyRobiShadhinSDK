package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import com.myrobi.shadhinmusiclibrary.data.repository.AuthRepository
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils.BASE_URL_API_shadhinmusic
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall

internal class RobiPaymentMethodRepository(
    private val paymentMethod: PaymentMethod.Robi,
    private val subscriptionApiService: SubscriptionApiService
):PaymentMethodRepository {
    override suspend fun subscriptionRequest(): SubscriptionResponse? {


        val robiUrl =  "${BASE_URL_API_shadhinmusic}RobiDCB/ReqSubsRDCB?mid=%s&subscriptionID=%s&chargetype=SMS&callbackurl=https://shadhinmusic.com/"
        val url = String.format(
            robiUrl,
            AuthRepository.msisdn,
            paymentMethod.selectedPlan?.serviceId
        )
        return SubscriptionResponse.Robi(url)
    }

    override suspend fun plans(): List<Plan>? {
        return SubscriptionConfig.robiPlans.values.toList()
    }

    override suspend fun cancel() {
        safeApiCall {
            subscriptionApiService.cancelRobi(mobileNo = AuthRepository.msisdn, subscriptionID = paymentMethod.selectedPlan?.serviceId)
        }
    }

}