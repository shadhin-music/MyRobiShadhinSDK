package com.myrobi.shadhinmusiclibrary.data.model.subscription

sealed class PaymentMethod(private val plan:Plan){
    class Robi(plan: Plan, val redirectURL:String?=null):PaymentMethod(plan)
    class Grameenphone(plan: Plan):PaymentMethod(plan)
    class Banglalink(plan: Plan):PaymentMethod(plan)
}
