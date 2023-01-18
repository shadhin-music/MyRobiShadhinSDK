package com.myrobi.shadhinmusiclibrary.data.model.subscription

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

sealed class Type(val duration: Duration) {
    class Daily:Type(1.days)
    class Monthly:Type(30.days)
    class HalfYearly:Type(182.days)
    class Yearly:Type(365.days)
    class Coupon(duration: Duration):Type(duration)
    class Custom(duration: Duration):Type(duration)
}


