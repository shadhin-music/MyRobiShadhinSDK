package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Keep
 sealed class Type(val name:String,val duration: Duration, ) {
    object Daily:Type("Daily",1.days)
    object Monthly:Type("Monthly",30.days)
    object HalfYearly:Type("HalfYearly",182.days)
    object Yearly:Type("Yearly",365.days)

    class Coupon(duration: Duration):Type("Coupon",duration)


}


