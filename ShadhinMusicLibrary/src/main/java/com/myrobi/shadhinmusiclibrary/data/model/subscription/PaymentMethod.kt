package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep

@Keep
internal sealed class PaymentMethod(val plan:Plan){
    class Robi(plan: Plan):PaymentMethod(plan)
    class Grameenphone(plan: Plan):PaymentMethod(plan)
    class Banglalink(plan: Plan):PaymentMethod(plan)
}
