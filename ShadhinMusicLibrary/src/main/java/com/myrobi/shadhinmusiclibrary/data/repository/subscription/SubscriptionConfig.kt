package com.myrobi.shadhinmusiclibrary.data.repository.subscription

import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Type
internal object SubscriptionConfig {

    val robiPlans = mapOf(
        "2250" to SubscriptionPlan(
            isAutoRenewal = true,
            amount = 2.00f,
            type = Type.Daily,
            extraVatText = "1% SC charge applicable"
        ),
        "2251" to SubscriptionPlan(
            isAutoRenewal = true,
            amount = 20.00f,
            type = Type.Monthly,
            extraVatText = "1% SC charge applicable"
        ),
        "2252" to SubscriptionPlan(
            isAutoRenewal = true,
            amount = 99.0f,
            type = Type.HalfYearly,
            extraVatText = "1% SC charge applicable"
        ),
        "2253" to SubscriptionPlan(
            isAutoRenewal = true,
            amount = 199.0f,
            type = Type.Yearly,
            extraVatText = "1% SC charge applicable"
        )
    )
    val allPlans: Map<String, Map<String, SubscriptionPlan>> = mapOf(
        "robi" to robiPlans
    )
    private var plans:Map<String, SubscriptionPlan>?=null
    init {
        val map: MutableMap<String, SubscriptionPlan> = LinkedHashMap()
        allPlans.values.forEach {
            map.putAll(it)
        }
        plans = map
    }

    fun findPlan(serviceId:String?): SubscriptionPlan? {
        return plans?.get(serviceId)
    }

}

