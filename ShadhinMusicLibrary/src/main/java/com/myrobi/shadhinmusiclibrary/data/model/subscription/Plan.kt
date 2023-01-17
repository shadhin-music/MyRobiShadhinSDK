package com.myrobi.shadhinmusiclibrary.data.model.subscription

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

sealed class Plan(val duration: Duration) {
    class Daily:Plan(1.days)
    class Monthly:Plan(30.days)
    class HalfYearly:Plan(182.days)
    class Yearly:Plan(365.days)
    class Coupon(duration: Duration):Plan(duration)
    class Custom(duration: Duration):Plan(duration)
}


