package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import com.myrobi.shadhinmusiclibrary.R
import java.io.Serializable

@Keep
internal sealed class PaymentMethod(val name:String?=null, val icon:Int?=null, val plan:Plan?=null): Serializable {
    class Robi(plan: Plan?=null):PaymentMethod(name = "Robi", R.drawable.ic_robi_charging,plan = plan)
    class Grameenphone(plan: Plan):PaymentMethod(plan = plan)
    class Banglalink(plan: Plan):PaymentMethod(plan = plan)
}
