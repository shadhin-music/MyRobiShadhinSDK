package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Keep
sealed class Type(val name:String,val duration: Duration, ) {
    class Daily:Type("Daily",1.days)
    class Monthly:Type("Monthly",30.days)
    class HalfYearly:Type("HalfYearly",182.days)
    class Yearly:Type("Yearly",365.days)
    class Coupon(duration: Duration):Type("Coupon",duration)
    class Custom(name:String,duration: Duration):Type(name,duration)

}


