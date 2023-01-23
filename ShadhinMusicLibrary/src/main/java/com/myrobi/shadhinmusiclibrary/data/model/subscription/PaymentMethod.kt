package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import com.myrobi.shadhinmusiclibrary.R
import java.io.Serializable

@Keep
internal sealed class PaymentMethod(val name:String?=null, val icon:Int?=null,var selectedPlan: Plan?=null): Serializable {
    class Robi(selectedPlan: Plan?=null):PaymentMethod(name = "Robi", R.drawable.ic_robi_charging,selectedPlan)
    class Grameenphone():PaymentMethod()
    class Banglalink():PaymentMethod()
}
