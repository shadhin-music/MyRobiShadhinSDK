package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import java.io.Serializable

@Keep
internal data class SubscriptionDetails(var paymentMethod: PaymentMethod?=null,val plans:List<Plan>? = null): Serializable
